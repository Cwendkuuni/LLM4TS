package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws JsonProcessingException {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 provider = new XmlSerializerProvider(objectMapper.getXmlRootNameLookup());
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, null);
 String result = gen.getOutputContent();
 assertEquals("<null/>", result);
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "Hello World";
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, value);
 String result = gen.getOutputContent();
 assertEquals("<string>Hello World</string>", result);
 }

 @Test
 public void testSerializeBoolean() throws IOException {
 boolean value = true;
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, value);
 String result = gen.getOutputContent();
 assertEquals("<boolean>true</boolean>", result);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 int value = 123;
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, value);
 String result = gen.getOutputContent();
 assertEquals("<number>123</number>", result);
 }

 @Test
 public void testSerializeList() throws IOException {
 Object[] list = new Object[]{"One", "Two", "Three"};
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, list);
 String result = gen.getOutputContent();
 assertEquals("<array name=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
         "<item name=\"item\">" +
         "<string>One</string>" +
         "</item>" +
         "<item name=\"item\">" +
         "<string>Two</string>" +
         "</item>" +
         "<item name=\"item\">" +
         "<string>Three</string>" +
         "</item>" +
         "</array>", result);
 }

 @Test
 public void testSerializeObjectWithRootName() throws IOException {
 MyClass obj = new MyClass("John Doe", 30);
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, obj, MyClass.class);
 String result = gen.getOutputContent();
 assertEquals("<person name=\"John Doe\" age=\"30\"/>", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithMissingRootName() throws IOException {
 MyClass obj = new MyClass("John Doe", 30);
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, obj);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithInvalidRootName() throws IOException {
 MyClass obj = new MyClass("John Doe", 30);
 SerializationConfig config = new SerializationConfig(objectMapper.getDefaultConfig());
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider = new XmlSerializerProvider(config, provider);
 config.setRootValueSerializer(new RootNameSerializer());
 provider.setRootValueSerializer(config.getRootValueSerializer());
 provider.serializeValue(gen, obj, MyClass.class);
 }

 @Test(expected = IOException.class)
 public void testSerializeInvalidObject() throws IOException {
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, new Object());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithNullRootType() throws IOException {
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.serializeValue(gen, new Object(), null);
 }

 @Test
 public void testSerializeObjectWithCustomSerializer() throws IOException {
 MyClassObjSerializer customSerializer = new MyClassObjSerializer();
 JsonGenerator gen = jsonFactory.createGenerator(new TokenStream(), null);
 provider.setSerializerInstance(MyClass.class, customSerializer);
 provider.serializeValue(gen, new MyClass("John Doe", 30), MyClass.class);
 String result = gen.getOutputContent();
 assertEquals("<myclass><name>John Doe</name><age>30</age></myclass>", result);
 }

}

class MyClass {
 private String name;
 private int age;

 public MyClass(String name, int age) {
 this.name = name;
 this.age = age;
 }
}

class MyClassObjSerializer extends JsonSerializer<MyClass> {
 @Override
 public void serialize(MyClass myclass, JsonGenerator jgen, SerializerProvider provider) throws IOException {
 jgen.writeStartElement("myclass");
 jgen.writeFieldName("name");
 jgen.writeString(myclass.getName());
 jgen.writeFieldName("age");
 jgen.writeNumber(myclass.getAge());
 jgen.writeEndElement();
 }
}