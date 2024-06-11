package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;

 @Before
 public void setUp() {
 provider = new XmlSerializerProvider(new XmlRootNameLookup());
 }

 // Test for serializeValue method with null input
 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = new StaxMapper().createXmlGenerator();
 provider.serializeValue(gen, null);
 String xmlOutput = gen.getBuffer().asString();
 assertEquals("<null/>", xmlOutput);
 }

 // Test for serializeValue method with a simple Object input
 @Test
 public void testSerializeSimpleObject() throws IOException {
 JsonGenerator gen = new StaxMapper().createXmlGenerator();
 String input = "Hello World";
 provider.serializeValue(gen, input);
 String xmlOutput = gen.getBuffer().asString();
 assertEquals("<string>Hello World</string>", xmlOutput);
 }

 // Test for serializeValue method with a JavaType input
 @Test
 public void testSerializeJavaType() throws IOException {
 JavaType javaType = new TypeFactory().constructType(String[].class);
 String[] inputArray = {"One", "Two", "Three"};
 provider.serializeValue(new StaxMapper().createXmlGenerator(), inputArray, javaType);
 String xmlOutput = new StaxMapper().createXmlParser().getTextValue(new StaxParser().parse(new StringReader(xmlOutput)));
 assertEquals("[<string>One</string><string>Two</string><string>Three</string>]", xmlOutput);
 }

 // Test for serializeValue method with a JsonSerializer provided
 @Test
 public void testSerializeWithJsonSerializer() throws IOException {
 JsonSerializer<String> jsonSerializer = new JsonSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
 throws IOException, JsonProcessingException {
 gen.writeString(value);
 }
 };
 provider.setSerializerInstance(String.class, jsonSerializer);
 JsonGenerator gen = new StaxMapper().createXmlGenerator();
 String input = "Hello World";
 provider.serializeValue(gen, input);
 String xmlOutput = gen.getBuffer().asString();
 assertEquals("<string>Hello World</string>", xmlOutput);
 }

 // Test for serializeValue method with an exception
 @Test(expected = JsonMappingException.class)
 public void testSerializeException() throws IOException {
 JsonGenerator gen = new StaxMapper().createXmlGenerator();
 provider.serializeValue(gen, new Object());
 }

 // Test for serializeValue method with an Array input and a rootName
 @Test
 public void testSerializeArrayWithRootName() throws IOException {
 String rootName = "myArray";
 JsonGenerator gen = new StaxMapper().createXmlGenerator();
 Object[] inputArray = {"One", "Two", "Three"};
 provider.serializeValue(gen, inputArray, new TypeFactory().constructType(String[].class), rootName);
 String xmlOutput = new StaxMapper().createXmlParser().getTextValue(new StaxParser().parse(new StringReader(xmlOutput)));
 assertEquals("<myArray><string>One</string><string>Two</string><string>Three</string></myArray>", xmlOutput);
 }

 // Test for serializeValue method with a TypeDeserializer provided
 @Test
 public void testSerializeWithTypeDeserializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDefaultSerializerProvider(provider);
 mapper.setDefaultTyping(DefaultTyping.OBJECT_ARRAY);
 String xmlInput = "<myArray><string>One</string><string>Two</string><string>Three</string></myArray>";
 MyClass[] inputArray = mapper.readValue(new StringReader(xmlInput), MyClass[].class);
 assertEquals(3, inputArray.length);
 }

}