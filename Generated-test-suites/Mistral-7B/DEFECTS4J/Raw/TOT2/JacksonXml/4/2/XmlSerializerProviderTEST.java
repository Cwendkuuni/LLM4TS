package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerMapper;
import com.fasterxml.jackson.databind.util.TestNameGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlProvider;
 private JsonSerializerProvider jsonProvider;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 SerializationConfig config = new SerializationConfig();
 SerializerFactory factory = new DefaultSerializerProvider.BaseSerializerFactory();
 xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new SimpleBeanPropertySerializerMapper(), new TestNameGenerator()));
 xmlProvider = new XmlSerializerProvider(xmlProvider, config, factory);
 jsonProvider = new DefaultSerializerProvider();
 mapper = new ObjectMapper(jsonProvider);
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, null);
 String xml = gen.getBaseStream().asString();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeBoolean() throws IOException {
 boolean value = true;
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, value);
 String xml = gen.getBaseStream().asString();
 assertEquals("<boolean>true</boolean>", xml);
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "John Doe";
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, value);
 String xml = gen.getBaseStream().asString();
 assertEquals("<string>John Doe</string>", xml);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 int value = 123;
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, value);
 String xml = gen.getBaseStream().asString();
 assertEquals("<int>123</int>", xml);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 double value = 123.45;
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, value);
 String xml = gen.getBaseStream().asString();
 assertEquals("<double>123.45</double>", xml);
 }

 @Test
 public void testSerializeObject() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Person.class);
 Person obj = new Person("John Doe", 30);
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, obj);
 String xml = gen.getBaseStream().asString();
 assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
 }

 @Test
 public void testSerializeArray() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Person[]>() {});
 Person[] obj = new Person[] { new Person("John Doe", 30), new Person("Jane Doe", 25) };
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, obj);
 String xml = gen.getBaseStream().asString();
 assertEquals("<person-array><person><name>John Doe</name><age>30</age></person><person><name>Jane Doe</name><age>25</age></person></person-array>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullRootName() throws IOException {
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownClass() throws IOException {
 JsonGenerator gen = mapper.getFactory().createXmlGenerator(null, false);
 xmlProvider.serializeValue(gen, new UnknownClass(), null);
 }
}