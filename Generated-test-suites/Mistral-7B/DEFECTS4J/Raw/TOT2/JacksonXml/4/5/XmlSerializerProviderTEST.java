package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new XMLFactory());
 provider = objectMapper.getXmlSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 provider.serializeValue(gen, null);
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeBoolean() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putBoolean("value", true);
 provider.serializeValue(gen, node, null);
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<value>true</value>", xml);
 }

 @Test
 public void testSerializeNumber() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putNumber("value", 123.456);
 provider.serializeValue(gen, node, null);
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<value>123.456</value>", xml);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putPOJO("value", new String("Hello"));
 provider.serializeValue(gen, node, null);
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<value>Hello</value>", xml);
 }

 @Test
 public void testSerializeArray() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode rootNode = provider.getFactory().createObjectNode();
 rootNode.putPOJO("items", new Object[] { "one", "two" });
 provider.serializeValue(gen, rootNode, ArrayType.construct(Object.class, "items"));
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<items><item>one</item><item>two</item></items>", xml);
 }

 @Test
 public void testSerializeCustomSerializer() throws JsonProcessingException {
 SimpleValueSerializer serializer = new SimpleValueSerializer(Object.class);
 JsonSerializer<Object> customSerializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
 throws IOException {
 gen.writeString("custom");
 }
 };
 objectMapper.registerSerializer(Object.class, customSerializer);
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putPOJO("value", new Object());
 provider.serializeValue(gen, node, null);
 String xml = gen.getOutputFinisher().asString();
 assertEquals("<value>custom</value>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueException() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putPOJO("value", new Object() {
 @Override
 public String toString() {
 throw new RuntimeException("Test Exception");
 }
 });
 provider.serializeValue(gen, node, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeRootNameException() throws JsonProcessingException {
 JsonGenerator gen = provider.getGenerator();
 ObjectNode node = provider.getFactory().createObjectNode();
 node.putPOJO("value", new Object());
 provider.setRootName(null);
 provider.serializeValue(gen, node, null);
 }
}