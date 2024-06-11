package com.example.xmlserializerprovider;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPreprendableByteArrayBuilder;
import com.fasterxml.jackson.core.util.DefaultPrependableStringBuilder;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.XmlSerializer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Test class for XmlSerializerProvider
 */
public class XmlSerializerProviderTest {

 private XmlRootNameLookup _rootNameLookup;
 private XmlSerializerProvider _xmlSerializerProvider;
 private SerializationConfig _config;
 private ObjectMapper _objectMapper;

 @Before
 public void setUp() {
 _rootNameLookup = new XmlRootNameLookup();
 _config = new SerializationConfig(new DefaultSerializerProvider(), null);
 _objectMapper = new ObjectMapper(_config);
 _xmlSerializerProvider = new XmlSerializerProvider(_rootNameLookup);
 }

 /**
 * Test method for {@link XmlSerializerProvider#XmlSerializerProvider(XmlRootNameLookup)}
 */
 @Test
 public void testXmlSerializerProvider() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(_rootNameLookup);
 assertNotNull(xmlSerializerProvider);
 }

 /**
 * Test method for {@link XmlSerializerProvider#copy()}
 */
 @Test
 public void testCopy() throws IOException {
 JsonGenerator gen = new DefaultPrependableStringBuilder().asXmlGenerator();
 Object value = new Object();
 _xmlSerializerProvider.serializeValue(gen, value);
 SerializerProvider newXmlSerializerProvider = _xmlSerializerProvider.copy();
 newXmlSerializerProvider.serializeValue(gen, value);
 assertEquals(_xmlSerializerProvider.toString(), newXmlSerializerProvider.toString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#createInstance(SerializationConfig, SerializerFactory)}
 */
 @Test
 public void testCreateInstance() throws IOException {
 JsonGenerator gen = new DefaultPrependableStringBuilder().asXmlGenerator();
 Object value = new Object();
 _xmlSerializerProvider = _xmlSerializerProvider.createInstance(_config, null);
 _xmlSerializerProvider.serializeValue(gen, value);
 assertNotSame(_xmlSerializerProvider, _xmlSerializerProvider.copy());
 }

 /**
 * Test method for {@link XmlSerializerProvider#createInstance(XmlSerializerProvider)}
 */
 @Test
 public void testCreateInstanceWithSameConfig() throws IOException {
 JsonGenerator gen = new DefaultPrependableStringBuilder().asXmlGenerator();
 Object value = new Object();
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(_rootNameLookup);
 XmlSerializerProvider newXmlSerializerProvider = xmlSerializerProvider.createInstance(xmlSerializerProvider);
 newXmlSerializerProvider.serializeValue(gen, value);
 assertEquals(_xmlSerializerProvider.toString(), newXmlSerializerProvider.toString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_serializeXmlNull(JsonGenerator)}
 */
 @Test
 public void testSerializeXmlNull() throws IOException {
 JsonGenerator gen = new DefaultPrependableStringBuilder().asXmlGenerator();
 _xmlSerializerProvider._serializeXmlNull(gen);
 String expectedXml = "<null></null>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_startRootArray(ToXmlGenerator, QName)}
 */
 @Test
 public void testStartRootArray() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 QName rootName = new QName("root", "name");
 _xmlSerializerProvider._startRootArray((ToXmlGenerator) gen, rootName);
 String expectedXml = "<root><item></item>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_initWithRootName(ToXmlGenerator, QName)}
 */
 @Test
 public void testInitWithRootName() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 QName rootName = new QName("root", "name");
 _xmlSerializerProvider._initWithRootName((ToXmlGenerator) gen, rootName);
 String expectedXml = "<root>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_rootNameFromConfig()}
 */
 @Test
 public void testRootNameFromConfig() {
 PropertyName name = new PropertyName("rootName");
 _config.setRootValueSeparator(new PropertyName("sep"));
 _config.setRootName(name);
 QName expectedRootName = new QName("rootName");
 QName rootName = _xmlSerializerProvider._rootNameFromConfig();
 assertEquals(expectedRootName.getNamespaceURI(), rootName.getNamespaceURI());
 assertEquals(expectedRootName.getLocalPart(), rootName.getLocalPart());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_asXmlGenerator(JsonGenerator)}
 */
 @Test
 public void testAsXmlGenerator() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 Object value = new Object();
 _xmlSerializerProvider.serializeValue(gen, value);
 assertFalse(_xmlSerializerProvider._asXmlGenerator(gen) instanceof ToXmlGenerator);
 }

 /**
 * Test method for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)}
 */
 @Test
 public void testSerializeValue() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 Object value = new Object();
 _xmlSerializerProvider.serializeValue(gen, value);
 String expectedXml = "<root><item></item>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer<Object>)}
 */
 @Test
 public void testSerializeValueWithRootType() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 Object value = new Object();
 JavaType rootType = _objectMapper.getTypeFactory().constructType(Object[].class);
 JsonSerializer<Object> ser = _objectMapper.getSerializerProvider().findValueSerializer(Object[].class, null);
 _xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
 String expectedXml = "<root><item></item>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#findTypedValueSerializer(Class, boolean, PropertyName)}
 */
 @Test
 public void testFindTypedValueSerializer() throws IOException {
 Class<String> stringClass = String.class;
 JsonSerializer<String> ser = _objectMapper.getSerializerProvider().findValueSerializer(stringClass, null);
 assertEquals(ser.getClass().getName(), _xmlSerializerProvider.getClassNames().get(stringClass).getName());
 }

 /**
 * Test method for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer<Object>, Boolean)}
 */
 @Test
 public void testSerializeValueWithCustomSerializer() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 Object value = new Object();
 JavaType rootType = _objectMapper.getTypeFactory().constructType(Object[].class);
 JsonSerializer<Object> ser = new XmlSerializer(_config, _objectMapper.getFactory());
 _xmlSerializerProvider.serializeValue(gen, value, rootType, ser, false);
 String expectedXml = "<root><item></item>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer<Object>, Boolean, Object)}
 */
 @Test
 public void testSerializeValueWithFilter() throws IOException {
 JsonGenerator gen = new DefaultPrependableByteArrayBuilder().asXmlGenerator();
 Object value = new Object();
 JavaType rootType = _objectMapper.getTypeFactory().constructType(Object[].class);
 JsonSerializer<Object> ser = _objectMapper.getSerializerProvider().findValueSerializer(Object[].class, null);
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
 _xmlSerializerProvider.serializeValue(gen, value, rootType, ser, false, filter);
 String expectedXml = "<root><item><name></name></item>";
 assertEquals(expectedXml, gen.getAsString());
 }

 /**
 * Test method for {@link XmlSerializerProvider#_wrapAsIOE(JsonGenerator, Exception)}
 */
 @Test
 public void testWrapAsIOE() {
 JsonGenerator gen = new DefaultPrependableStringBuilder().asXmlGenerator();
 Exception e = new RuntimeException();
 IoException expectedException = new IoException("Exception message", gen, e);
 assertEquals(expectedException.getMessage(), _xmlSerializerProvider._wrapAsIOE(gen, e).getMessage());
 }
}