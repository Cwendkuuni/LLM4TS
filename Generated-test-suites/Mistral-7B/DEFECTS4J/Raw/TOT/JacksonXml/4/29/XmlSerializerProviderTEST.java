package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private static final ObjectMapper MAPPER = new XmlMapper();

 @SuppressWarnings("unchecked")
 private static <T> T createObjectMapperWithRootNameLookup(Class<T> mapperType, XmlRootNameLookup rootNames) {
 SimpleModule module = new SimpleModule(mapperType.getName(), MAPPER.getSerializerProvider().getClass())
 .addSerializerModifier(new StdSerializerModifier());
 if (rootNames != null) {
 module.setSerializerProvider(new XmlSerializerProvider(rootNames));
 }
 return (T) new XmlMapper(MAPPER.getFactory(), module);
 }

 @org.junit.Test
 public void testSerialization() throws JsonMappingException, JsonProcessingException, IOException {
 // Test serialization of simple types
 SerializerProvider serProvider = MAPPER.getSerializerProvider();
 SerializationConfig config = new SerializationConfig(serProvider);
 JsonGenerator gen = config.getFactory().createGenerator(System.out);
 ObjectNode rootNode = MAPPER.createObjectNode();
 rootNode.put("string", "hello");
 rootNode.put("int", 123);
 rootNode.put("boolean", true);
 serProvider.writeValueAsXml(gen, rootNode);
 gen.flush();
 gen.close();

 // Test serialization with custom root name
 XmlRootNameLookup rootNames = new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return new QName("customRoot", "");
 }
 };
 ObjectMapper mapper = createObjectMapperWithRootNameLookup(XmlMapper.class, rootNames);
 rootNode = mapper.readTree("{\"string\":\"hello\",\"int\":123,\"boolean\":true}");
 gen = mapper.getFactory().createGenerator(System.out);
 gen.writeStartDocument("UTF-8", true);
 gen.writeStartElement("customRoot");
 mapper.writeValueAsXml(gen, rootNode);
 gen.writeEndDocument();
 gen.flush();
 gen.close();

 // Test serialization of null value
 rootNode = MAPPER.createObjectNode();
 rootNode.putNull("null");
 serProvider = MAPPER.getSerializerProvider();
 gen = serProvider.getFactory().createGenerator(System.out);
 serProvider.serializeValue(gen, rootNode.get("null"));
 gen.flush();
 gen.close();

 // Test serialization of null value with custom root name
 rootNames = new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return ROOT_NAME_FOR_NULL;
 }
 };
 mapper = createObjectMapperWithRootNameLookup(XmlMapper.class, rootNames);
 rootNode = mapper.readTree("{\"null\":null}");
 gen = mapper.getFactory().createGenerator(System.out);
 gen.writeStartDocument("UTF-8", true);
 gen.writeStartElement("null");
 mapper.writeValueAsXml(gen, rootNode.get("null"));
 gen.writeEndDocument();
 gen.flush();
 gen.close();

 // Test serialization of complex objects
 SimpleModule module = new SimpleModule("TestModule", serProvider.getClass());
 module.addSerializer(String.class, new StringSerializer());
 module.setSerializerProvider(new XmlSerializerProvider(null));
 ObjectMapper mapper2 = new ObjectMapper(serProvider, module);
 MyClass myClass = new MyClass("test", Arrays.asList(1, 2, 3));
 ObjectNode rootNode2 = mapper2.valueToTree(myClass);
 gen = mapper2.getFactory().createGenerator(System.out);
 gen.writeStartDocument("UTF-8", true);
 gen.writeStartElement("MyClass");
 mapper2.writeValueAsXml(gen, rootNode2);
 gen.writeEndDocument();
 gen.flush();
 gen.close();
 }

 static class MyClass {
 private final String name;
 private final java.util.List<Integer> numbers;

 public MyClass(String name, java.util.List<Integer> numbers) {
 this.name = name;
 this.numbers = numbers;
 }
 }

 static class StringSerializer extends StdSerializer<String> {
 protected StringSerializer() {
 this(null);
 }

 protected StringSerializer(Class<String> t) {
 super(t);
 }

 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
 throws IOException {
 gen.writeString(value);
 }
 }
}