package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private static ObjectMapper objectMapper;
 private static SerializationConfig config;
 private static XmlRootNameLookup rootNameLookup;
 private static XmlSerializerProvider xmlSerializerProvider;

 @BeforeClass
 public static void setup() throws Exception {
 objectMapper = new XmlMapper();
 config = objectMapper.getSerializationConfig();
 rootNameLookup = new XmlRootNameLookup();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testCreateInstance() throws Exception {
 SerializerProvider provider = xmlSerializerProvider.createInstance(config, null);
 assertTrue(provider instanceof XmlSerializerProvider);
 }

 @Test
 public void testSerializeValueNull() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 try {
 xmlSerializerProvider.serializeValue(generator, null);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<null/>";
 assertEquals(expectedXml, actualXml);
 } finally {
 generator.close();
 outputStream.close();
 }
 }

 @Test
 public void testSerializeValueBasic() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 xmlSerializerProvider.serializeValue(generator, value);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>";
 assertTrue(actualXml.startsWith(expectedXml));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithEmptyRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 xmlSerializerProvider.serializeValue(generator, value, new JavaType(Object.class));
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueWithRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 QName rootName = new QName("root");
 xmlSerializerProvider.serializeValue(generator, value, rootName);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><root/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueSerializers() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new MyClass();
 xmlSerializerProvider.serializeValue(generator, value);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><myClass><field>value</field></myClass>";
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueNotFoundSerializer() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new MyOtherClass();
 xmlSerializerProvider.serializeValue(generator, value);
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueArray() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object[] values = {new Object(), new Object()};
 xmlSerializerProvider.serializeValue(generator, values);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><item><root/></item><item><root/></item>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueArrayRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object[] values = {new Object(), new Object()};
 QName rootName = new QName("itemList");
 xmlSerializerProvider.serializeValue(generator, values, rootName);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<itemList><item><root/></item><item><root/></item>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueArrayWithNullElements() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object[] values = {null, new Object()};
 xmlSerializerProvider.serializeValue(generator, values);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><item>null</item><item><root/></item>";
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueArrayNotFoundSerializer() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object[] values = {new MyOtherClass(), new Object()};
 xmlSerializerProvider.serializeValue(generator, values);
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueArrayWithRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object[] values = {new Object(), new Object()};
 QName rootName = new QName("itemList");
 xmlSerializerProvider.serializeValue(generator, values, rootName);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<itemList><item><root/></item><item><root/></item>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueWithEmptyRootType() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 JavaType rootType = null;
 xmlSerializerProvider.serializeValue(generator, value, rootType);
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueWithRootType() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 JavaType rootType = new TypeReference<ObjectNode>() {}.getType();
 xmlSerializerProvider.serializeValue(generator, value, rootType);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><root/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueWithRootTypeAndSerializer() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 JavaType rootType = new TypeReference<ObjectNode>() {}.getType();
 JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("value");
 }
 };
 xmlSerializerProvider.serializeValue(generator, value, rootType, serializer);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>value</root>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueWithNullRootType() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 JavaType rootType = null;
 JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("value");
 }
 };
 xmlSerializerProvider.serializeValue(generator, value, rootType, serializer);
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueWithNullRootTypeAndSerializer() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = new Object();
 xmlSerializerProvider.serializeValue(generator, value, null, null);
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueWithNullValue() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = null;
 xmlSerializerProvider.serializeValue(generator, value, new JavaType(Object.class));
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<null/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueWithNullValueAndRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = null;
 QName rootName = new QName("root");
 xmlSerializerProvider.serializeValue(generator, value, rootName);
 generator.flush();
 String actualXml = outputStream.toString();
 String expectedXml = "<root><null/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueWithNullValueAndRootType() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = null;
 JavaType rootType = new TypeReference<ObjectNode>() {}.getType();
 xmlSerializerProvider.serializeValue(generator, value, rootType);
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testSerializeValueWithNullValueAndRootTypeAndSerializer() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 Object value = null;
 JavaType rootType = new TypeReference<ObjectNode>() {}.getType();
 JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("value");
 }
 };
 xmlSerializerProvider.serializeValue(generator, value, rootType, serializer);
 generator.flush();
 fail("Expected JsonMappingException, but got: "+outputStream.toString());
 }

 @Test
 public void testInitializeWithRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 xmlSerializerProvider.initWithRootName((ToXmlGenerator) generator, ROOT_NAME_FOR_NULL);
 String actualXml = outputStream.toString();
 String expectedXml = "<null/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testInitializeWithRootNameAndNamespace() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 String namespace = "http://example.com/";
 xmlSerializerProvider.initWithRootName((ToXmlGenerator) generator, new QName("root", namespace));
 String actualXml = outputStream.toString();
 String expectedXml = "<root xmlns=\""+namespace+"\"><root/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testStartRootArray() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 xmlSerializerProvider.startRootArray((ToXmlGenerator) generator, ROOT_NAME_FOR_NULL);
 String actualXml = outputStream.toString();
 String expectedXml = "<null/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testStartRootArrayWithRootName() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, "UTF-8");
 QName rootName = new QName("itemList");
 xmlSerializerProvider.startRootArray((ToXmlGenerator) generator, rootName);
 String actualXml = outputStream.toString();
 String expectedXml = "<itemList/>";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testFindRootName() throws Exception {
 Class<?> clazz = MyClass.class;
 JavaType javaType = new TypeReference<ObjectNode>() {}.getType();
 assertEquals("MyClass", xmlSerializerProvider.findRootName(clazz, config).getLocalPart());
 assertEquals("MyClass", xmlSerializerProvider.findRootName(javaType, config).getLocalPart());
 }

 @Test
 public void testFindRootNameWithNullConfig() throws Exception {
 Class<?> clazz = MyClass.class;
 JavaType javaType = new TypeReference<ObjectNode>() {}.getType();
 assertNull(xmlSerializerProvider.findRootName(clazz, null));
 assertNull(xmlSerializerProvider.findRootName(javaType, null));
 }

 @Test
 public void testFindRootNameWithEmptyConfig() throws Exception {
 Class<?> clazz = MyClass.class;
 JavaType javaType = new TypeReference<ObjectNode>() {}.getType();
 SerializationConfig config = new SerializationConfig();
 assertEquals("MyClass", xmlSerializerProvider.findRootName(clazz, config).getLocalPart());
 assertEquals("MyClass", xmlSerializerProvider.findRootName(javaType, config).getLocalPart());
 }

 @Test
 public void testFindRootNameWithNullClass() throws Exception {
 JavaType javaType = new TypeReference<ObjectNode>() {}.getType();
 assertEquals(null, xmlSerializerProvider.findRootName(null, config));
 }

 @Test
 public void testFindRootNameWithNullType() throws Exception {
 SerializationConfig config = new SerializationConfig();
 assertEquals(null, xmlSerializerProvider.findRootName(null, config));
 }

 @Test
 public void testFindRootNameWithEmptyType() throws Exception {
 SerializationConfig config = new SerializationConfig();
 assertEquals(null, xmlSerializerProvider.findRootName(new TypeReference<ObjectNode>() {}.getType(), config));
 }

 private static class MyClass {
 private String field;
 }

 private static class MyOtherClass {
 private int field;
 }
}