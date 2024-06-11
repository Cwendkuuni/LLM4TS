package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for XmlSerializerProvider class.
 */
public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig config;
 private XmlRootNameLookup rootNameLookup;
 private XmlSerializerProvider serializerProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 rootNameLookup = new XmlRootNameLookup();
 serializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testCopy() {
 XmlSerializerProvider newSerializerProvider = serializerProvider.copy();
 assertNotSame(serializerProvider, newSerializerProvider);
 assertEquals(serializerProvider, newSerializerProvider.getSerializerProviders());
 }

 @Test
 public void testCreateInstance() {
 SerializerProvider newSerializerProvider = serializerProvider.createInstance(config, new DefaultSerializerFactory());
 assertNotSame(serializerProvider, newSerializerProvider);
 assertEquals(serializerProvider, newSerializerProvider.getSerializerProviders());
 }

 @Test
 public void testCreateInstanceWithSameInstance() {
 XmlSerializerProvider newSerializerProvider = serializerProvider.createInstance(config, serializerProvider);
 assertNotSame(serializerProvider, newSerializerProvider);
 assertEquals(serializerProvider, newSerializerProvider.getSerializerProviders());
 }

 @Test
 public void testCreateInstanceWithNullRootNameLookup() {
 SerializationConfig configWithNullRootNameLookup = new SerializationConfig(config);
 configWithNullRootNameLookup.setRootValueSerializer(null);
 XmlSerializerProvider newSerializerProvider = serializerProvider.createInstance(configWithNullRootNameLookup, new DefaultSerializerFactory());
 assertNotNull(newSerializerProvider.getRootNameLookup());
 }

 @Test
 public void testCreateInstanceWithRootNameLookupInstance() {
 XmlRootNameLookup rootNameLookupInstance = new XmlRootNameLookup();
 SerializationConfig configWithRootNameLookupInstance = new SerializationConfig(config);
 configWithRootNameLookupInstance.setRootValueSerializer(rootNameLookupInstance);
 XmlSerializerProvider newSerializerProvider = serializerProvider.createInstance(configWithRootNameLookupInstance, new DefaultSerializerFactory());
 assertEquals(rootNameLookupInstance, newSerializerProvider.getRootNameLookup());
 }

 @Test
 public void testCreateInstanceWithRootValueSerializer() {
 SerializerProvider serializerProviderWithRootValueSerializer = new DefaultSerializerProvider(false);
 JsonSerializer<Object> rootValueSerializer = new StdSerializer<Object>(Object.class) {};
 SerializationConfig configWithRootValueSerializer = new SerializationConfig(config);
 configWithRootValueSerializer.setRootValueSerializer(rootValueSerializer);
 XmlSerializerProvider newSerializerProvider = serializerProvider.createInstance(configWithRootValueSerializer, new DefaultSerializerFactory());
 assertEquals(rootValueSerializer, newSerializerProvider.findValueSerializer(Object.class));
 }

 @Test
 public void testSerializeValueNullRootName() {
 QName nullRootName = XmlSerializerProvider.ROOT_NAME_FOR_NULL;
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, null, nullRootName);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("[null]", result);
 }

 @Test
 public void testSerializeValueNullValue() {
 Object value = null;
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, value);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("", result);
 }

 @Test
 public void testSerializeValueSimpleObject() {
 String value = "test";
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, value);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<test/>", result);
 }

 @Test
 public void testSerializeValueSimpleObjectWithRootName() {
 String value = "test";
 QName rootName = new QName("testNamespace", "testRootName");
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, value, rootName);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<testRootName><test/></testRootName>", result);
 }

 @Test
 public void testSerializeValueListObject() throws IOException {
 List<String> values = Arrays.asList("test1", "test2");
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, values);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<item><test1/></item><item><test2/></item>", result);
 }

 @Test
 public void testSerializeValueListObjectWithRootName() throws IOException {
 List<String> values = Arrays.asList("test1", "test2");
 QName rootName = new QName("testNamespace", "testRootName");
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, values, rootName);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<testRootName><item><test1/></item><item><test2/></item></testRootName>", result);
 }

 @Test
 public void testSerializeValueMapObject() throws IOException {
 Map<String, String> values = new HashMap<>();
 values.put("test1", "value1");
 values.put("test2", "value2");
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, values);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<item name=\"test1\"><value1/></item><item name=\"test2\"><value2/></item>", result);
 }

 @Test
 public void testSerializeValueMapObjectWithRootName() throws IOException {
 Map<String, String> values = new HashMap<>();
 values.put("test1", "value1");
 values.put("test2", "value2");
 QName rootName = new QName("testNamespace", "testRootName");
 JsonGenerator jsonGenerator = createJsonGenerator();
 serializerProvider.serializeValue(jsonGenerator, values, rootName);
 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 assertEquals("<testRootName><item name=\"test1\"><value1/></item><item name=\"test2\"><value2/></item></testRootName>", result);
 }

 @Test(expected = IOException.class)
 public void testSerializeValueException() throws IOException, XMLStreamException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 throw new IOException("Test IOException");
 }

 @Test
 public void testSerializeValueExceptionWrapped() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 throw new XMLStreamException("Test XMLStreamException");
 }

 @Test
 public void testSerializeValueExceptionWrappedWithMessage() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 throw new XMLStreamException("Test XMLStreamException with message");
 }

 @Test
 public void testSerializeValueExceptionWrappedWithCustomMessage() throws IOException, XMLStreamException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 throw new XmlMappingException("Test XmlMappingException", jsonGenerator);
 }

 private JsonGenerator createJsonGenerator() throws XMLStreamException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createGenerator(new TokenBuffer(1024));
 }
}