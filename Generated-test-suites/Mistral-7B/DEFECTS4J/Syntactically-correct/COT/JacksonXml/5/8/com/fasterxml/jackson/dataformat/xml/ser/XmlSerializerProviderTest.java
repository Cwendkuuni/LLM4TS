package com.example.xmlserializerprovider;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private JsonFactory jsonFactory;
 private XmlRootNameLookup rootNameLookup;

 @Before
 public void setUp() throws Exception {
 jsonFactory = new JsonFactory();
 rootNameLookup = new XmlRootNameLookup();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testXmlSerializerProviderConstructor() {
 XmlSerializerProvider xsp = new XmlSerializerProvider(rootNameLookup);
 assertNotNull(xsp);
 }

 @Test
 public void testCopy() {
 XmlSerializerProvider xsp1 = new XmlSerializerProvider(xmlSerializerProvider);
 XmlSerializerProvider xsp2 = new XmlSerializerProvider(xmlSerializerProvider);
 assertNotSame(xsp1, xsp2);
 assertSame(xsp1, xmlSerializerProvider);
 assertSame(xsp2, xmlSerializerProvider);
 }

 @Test
 public void testCreateInstance() throws Exception {
 SerializationConfig config = new SerializationConfig(jsonFactory);
 SerializerFactory factory = new DefaultSerializerFactory(jsonFactory);
 XmlSerializerProvider xsp = new XmlSerializerProvider(xmlSerializerProvider, config, factory);
 assertNotNull(xsp);
 }

 @Test
 public void testSerializationWithNullValue() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(null, new ObjectWriter().getRootValueInstance(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT), null));
 assertEquals("null", rootNode.get("item").asText());
 }

 @Test
 public void testSerializationWithSimpleValue() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 String value = "Hello, World!";
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(value, value.getClass(), null));
 assertEquals("Hello, World!", rootNode.get("item").asText());
 }

 @Test
 public void testSerializationWithComplexValue() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonNode complexValue = JsonNodeFactory.instance.objectNode();
 complexValue.put("name", "John Doe");
 complexValue.put("age", 30);
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(complexValue, complexValue.getClass(), null));
 assertEquals("John Doe", rootNode.get("item").get("name").asText());
 assertEquals(30, rootNode.get("item").get("age").asInt());
 }

 @Test
 public void testSerializationWithArrayValue() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 int[] arrayValue = new int[] {1, 2, 3};
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(arrayValue, arrayValue.getClass(), null));
 assertArrayEquals(new int[] {1, 2, 3}, rootNode.get("item"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidGenerator() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonGenerator gen = objectWriter.getGenerator();
 xmlSerializerProvider.serializeValue(gen, null);
 fail("Expected JsonMappingException");
 }

 @Test(expected = IOException.class)
 public void testSerializationWithIOException() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonGenerator gen = objectWriter.getGenerator();
 when(gen.writeStartObject()).thenThrow(new IOException("Test IOException"));
 xmlSerializerProvider.serializeValue(gen, null);
 fail("Expected IOException");
 }

 @Test
 public void testSerializationWithNullRootName() {
 XmlSerializerProvider xsp = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override public QName findRootName(Class<?> cls, SerializationConfig config) {
 return null;
 }});
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 String value = "Hello, World!";
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue(value, value.getClass(), null));
 assertNull(rootNode.get("item"));
 }

 @Test
 public void testSerializationWithEmptyRootName() throws Exception {
 XmlSerializerProvider xsp = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override public QName findRootName(Class<?> cls, SerializationConfig config) {
 return new QName("");
 }});
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 String value = "Hello, World!";
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue(value, value.getClass(), null));
 assertEquals("", rootNode.get("item").asText());
 }

 @Test
 public void testSerializationWithRootName() throws Exception {
 XmlSerializerProvider xsp = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override public QName findRootName(Class<?> cls, SerializationConfig config) {
 return new QName("rootName");
 }});
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 String value = "Hello, World!";
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue(value, value.getClass(), null));
 assertEquals("rootName", rootNode.get("item").getName());
 }

 @Test
 public void testSerializationWithRootNameAndNamespace() throws Exception {
 XmlSerializerProvider xsp = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override public QName findRootName(Class<?> cls, SerializationConfig config) {
 return new QName("rootName", "http://example.com");
 }});
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 String value = "Hello, World!";
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue(value, value.getClass(), null));
 assertEquals("rootName", rootNode.get("item").getName());
 assertEquals("http://example.com", rootNode.get("item").getNamespace().getNamespaceURI());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullRootType() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(null, null, null));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullSerializer() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(new ObjectWriter().getRootValueInstance(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT), null, null));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullValueSerializer() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(new ObjectWriter().getRootValueInstance(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT), JsonType.construct(Object.class), null));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidSerializerForValueType() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
 @Override public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not be called");
 }};
 JsonNode rootNode = objectWriter.writeValueAsTree(xmlSerializerProvider.serializeValue(new ObjectWriter().getRootValueInstance(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT), JsonType.construct(String.class), serializer));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullRootNameLookup() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 XmlSerializerProvider xsp = new XmlSerializerProvider(null);
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue("Hello, World!", String.class, null));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullSerializationConfig() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 XmlSerializerProvider xsp = new XmlSerializerProvider(rootNameLookup);
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue("Hello, World!", String.class, null, null));
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullSerializerFactory() throws Exception {
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();
 XmlSerializerProvider xsp = new XmlSerializerProvider(rootNameLookup, null, null);
 JsonNode rootNode = objectWriter.writeValueAsTree(xsp.serializeValue("Hello, World!", String.class, null));
 fail("Expected JsonMappingException");
 }
}