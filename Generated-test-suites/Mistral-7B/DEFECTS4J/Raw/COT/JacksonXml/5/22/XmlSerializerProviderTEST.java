package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Objects;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup.RootName;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlProvider;
 private XmlRootNameLookup rootNameLookup;
 private JsonGenerator gen;
 private SerializationConfig config;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 rootNameLookup = mock(XmlRootNameLookup.class);
 xmlProvider = new XmlSerializerProvider(rootNameLookup);
 config = new SerializationConfig();
 objectMapper = new ObjectMapper(config);
 gen = mock(JsonGenerator.class);
 }

 @Test
 public void testXmlSerializerProviderConstructor() {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
 assertNotNull(xmlProvider);
 }

 @Test
 public void testCopyConstructor() throws IOException {
 XmlSerializerProvider xmlProvider1 = new XmlSerializerProvider(this.xmlProvider);
 assertNotNull(xmlProvider1);
 assertNotNull(xmlProvider1._rootNameLookup);
 assertSame(xmlProvider._rootNameLookup, xmlProvider1._rootNameLookup);
 }

 @Test
 public void testCreateInstance() throws IOException {
 SerializerProvider provider = xmlProvider.createInstance(config, objectMapper.getFactory());
 assertNotNull(provider);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 xmlProvider.serializeValue(gen, null);
 }

 @Test
 public void testSerializeValueObject() throws IOException, JsonProcessingException {
 Object value = new Object();
 jsonProvider.serializeValue(gen, value);
 }

 @Test
 public void testSerializeValueObjectWithRootType() throws IOException, JsonProcessingException {
 JavaType rootType = objectMapper.getTypeFactory().constructType(Object[].class);
 jsonProvider.serializeValue(gen, value, rootType, null);
 }

 @Test
 public void testSerializeValueObjectWithSerializer() throws IOException, JsonProcessingException {
 Object value = new Object();
 JsonSerializer<Object> serializer = mock(JsonSerializer.class);
 jsonProvider.serializeValue(gen, value, null, serializer);
 }

 @Test
 public void testRootNameFromConfig() {
 String rootName = xmlProvider._rootNameFromConfig().getLocalPart();
 assertEquals("", rootName);
 }

 @Test
 public void testRootNameFromConfigWithCustomRootName() throws IOException {
 PropertyName rootName = new PropertyName("customRootName");
 config.setRootValueSeparator("");
 config.setRootName(rootName);
 String rootNameFromConfig = xmlProvider._rootNameFromConfig().getLocalPart();
 assertEquals("customRootName", rootNameFromConfig);
 }

 @Test
 public void testRootNameLookup() throws IOException {
 QName rootName = rootNameLookup.findRootName(Object[].class, config);
 assertNotNull(rootName);
 }

 @Test(expected = JsonMappingException.class)
 public void testRootNameLookupWithInvalidRootName() throws IOException {
 when(rootNameLookup.findRootName(String.class, config)).thenReturn(new QName("", ""));
 jsonProvider.serializeValue(gen, new String("HelloWorld"));
 }

 @Test
 public void testInitializeWithRootName() throws IOException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 QName rootName = new QName("rootName");
 xmlProvider._initWithRootName(xgen, rootName);
 assertEquals("rootName", xgen.getName());
 }

 @Test
 public void testInitializeWithNullRootName() throws IOException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 xmlProvider._initWithRootName(xgen, null);
 assertNull(xgen.getName());
 }

 @Test
 public void testStartRootArray() throws IOException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 xmlProvider._startRootArray(xgen, new QName("rootName"));
 assertEquals("item", xgen.getName());
 }
}