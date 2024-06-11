package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private XmlRootNameLookup _rootNameLookup;
 private ObjectMapper _objectMapper;
 private XmlMapper _xmlMapper;
 private SerializerProvider _serializerProvider;
 private SerializationConfig _config;
 private SerializerFactory _serializerFactory;

 @Before
 public void setup() {
 _rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
 _objectMapper = new ObjectMapper();
 _xmlMapper = new XmlMapper();
 _config = new SerializationConfig(_objectMapper.getFactory());
 _serializerFactory = _xmlMapper.getSerializerProvider().getFactory();
 _serializerProvider = new XmlSerializerProvider(_rootNameLookup);
 }

 // Test for constructor
 @Test
 public void testXmlSerializerProviderConstruction() {
 assertEquals(_rootNameLookup, _serializerProvider.getRootNameLookup());
 }

 // Test for createInstance method
 @Test
 public void testCreateInstance() throws IOException {
 XmlSerializerProvider newInstance = _serializerProvider.createInstance(_config, _serializerFactory);
 assertEquals(_serializerProvider, newInstance);
 }

 // Test for serializeValue method when value is null
 @Test
 public void testSerializeValueWithNullValue() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider.serializeValue(gen, null);
 Mockito.verify(gen, Mockito.times(1)).writeStartElement("null");
 }

 // Test for serializeValue method when value is not null
 @Test
 public void testSerializeValueWithValue() throws IOException {
 Object value = new Object();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider.serializeValue(gen, value);
 ObjectNode actualResult = _objectMapper.readTree(_xmlMapper.writeValueAsString(gen));
 ObjectNode expectedResult = _objectMapper.createObjectNode();
 expectedResult.put("item", _objectMapper.valueToTree(value));
 assertEquals(expectedResult, actualResult);
 }

 // Test for serializeValue method when value is an array
 @Test
 public void testSerializeValueWithArray() throws IOException {
 Object[] values = new Object[]{new Object(), new Object()};
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider.serializeValue(gen, values);
 ObjectNode actualResult = _objectMapper.readTree(_xmlMapper.writeValueAsString(gen));
 ObjectNode expectedResult = _objectMapper.createObjectNode();
 expectedResult.put("item", _objectMapper.valueToTree(values));
 assertEquals(expectedResult, actualResult);
 }

 // Test for serializeValue method when value is an array with root type
 @Test
 public void testSerializeValueWithArrayAndRootType() throws IOException {
 Object[] values = new Object[]{new Object(), new Object()};
 JavaType rootType = Mockito.mock(JavaType.class);
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider.serializeValue(gen, values, rootType);
 ObjectNode actualResult = _objectMapper.readTree(_xmlMapper.writeValueAsString(gen));
 ObjectNode expectedResult = _objectMapper.createObjectNode();
 expectedResult.put("item", _objectMapper.valueToTree(values));
 assertEquals(expectedResult, actualResult);
 }

 // Test for serializeValue method when value is an array with root type and serializer
 @Test
 public void testSerializeValueWithArrayAndRootTypeAndSerializer() throws IOException {
 Object[] values = new Object[]{new Object(), new Object()};
 JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
 JavaType rootType = Mockito.mock(JavaType.class);
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider.serializeValue(gen, values, rootType, serializer);
 ObjectNode actualResult = _objectMapper.readTree(_xmlMapper.writeValueAsString(gen));
 ObjectNode expectedResult = _objectMapper.createObjectNode();
 expectedResult.put("item", _objectMapper.valueToTree(values));
 assertEquals(expectedResult, actualResult);
 }

 // Test for _serializeXmlNull method
 @Test
 public void test_serializeXmlNull() throws IOException, XMLStreamException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 _serializerProvider._serializeXmlNull(gen);
 Mockito.verify(gen, Mockito.times(1)).writeStartElement("null");
 }

 // Test for _initWithRootName method
 @Test
 public void test_initWithRootName() throws IOException {
 ObjectNode rootNode = _objectMapper.createObjectNode();
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 QName rootName = new QName("rootName");
 _serializerProvider._initWithRootName(xgen, rootName);
 Mockito.verify(xgen, Mockito.times(1)).setNextName("rootName");
 }

 // Test for _initWithRootName method when name is already set
 @Test
 public void test_initWithRootNameWhenNameIsAlreadySet() throws IOException {
 ObjectNode rootNode = _objectMapper.createObjectNode();
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 xgen.setNextName("existingName");
 _serializerProvider._initWithRootName(xgen, new QName("rootName"));
 Mockito.verify(xgen, Mockito.times(0)).setNextName("rootName");
 }

 // Test for _initWithRootName method when in root
 @Test
 public void test_initWithRootNameWhenInRoot() throws IOException {
 ObjectNode rootNode = _objectMapper.createObjectNode();
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 _serializerProvider._initWithRootName(xgen, new QName("rootName"));
 Mockito.verify(xgen, Mockito.times(1)).writeStartObject();
 }

 // Test for _rootNameFromConfig method
 @Test
 public void test_rootNameFromConfig() {
 PropertyName name = new PropertyName("rootName");
 QName result = _serializerProvider._rootNameFromConfig();
 assertEquals(new QName("rootName"), result);
 }

 // Test for _rootNameFromConfig method when config is null
 @Test(expected = NullPointerException.class)
 public void test_rootNameFromConfigWhenConfigIsNull() {
 _serializerProvider._rootNameFromConfig();
 }

 // Test for _asXmlGenerator method
 @Test
 public void test_asXmlGenerator() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator result = _serializerProvider._asXmlGenerator(gen);
 assertNull(result);
 }

 // Test for _asXmlGenerator method when gen is not a ToXmlGenerator
 @Test
 public void test_asXmlGeneratorWhenGenIsNotToXmlGenerator() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 assertNull(_serializerProvider._asXmlGenerator(gen));
 }

 // Test for _asXmlGenerator method when gen is a ToXmlGenerator
 @Test
 public void test_asXmlGeneratorWhenGenIsToXmlGenerator() throws IOException {
 JsonGenerator gen = Mockito.mock(ToXmlGenerator.class);
 ToXmlGenerator result = _serializerProvider._asXmlGenerator(gen);
 assertEquals(gen, result);
 }

 // Test for _startRootArray method
 @Test
 public void test_startRootArray() throws IOException {
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 _serializerProvider._startRootArray(xgen, new QName("rootName"));
 Mockito.verify(xgen, Mockito.times(1)).writeStartObject();
 Mockito.verify(xgen, Mockito.times(1)).writeFieldName("item");
 }
}