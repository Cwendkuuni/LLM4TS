package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private InnerClassProperty property;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 BeanPropertyDefinition def = new BeanPropertyDefinition(
 Object.class.getDeclaredField("innerClassField"), null, null);
 Class<?> nestedClass = NestedClass.class;
 Constructor<?> constructor = nestedClass.getDeclaredConstructor();
 property = new InnerClassProperty(
 new SettableBeanProperty(def), constructor);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object bean = new Object();
 ObjectNode node = ObjectMapperTestUtil.createObjectNode(
 new Object() {{
 innerClassField = new NestedClass();
 }}
 );
 Object deserializedValue = new Object();
 JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
 JsonDeserializer<Class<?>> typeDeserializer = new StubJsonDeserializer();
 property.setValueDeserializer(deserializer);
 property.setValueSerializer(new StubJsonSerializer());
 property.setValueTypeDeserializer(typeDeserializer);
 objectMapper.registerValueDeserializer(NestedClass.class, deserializer);
 objectMapper.registerValueDeserializer(Class.class, typeDeserializer);
 property.setName("innerClassProperty");
 property.assignIndex(0);
 Object result = objectMapper.readValue(node.traverse(), Object.class);
 assertEquals("expected object with set innerClassProperty", result, bean);
 property.deserializeAndSet(ObjectMapperTestUtil.createParser(node), null, bean);
 NestedClass nested = (NestedClass) bean.getClass().getField("innerClassField").get(bean);
 assertNotNull("nested class should not be null", nested);
 assertEquals("deserialized value should match original", deserializedValue, nested);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object bean = new Object();
 ObjectNode node = ObjectMapperTestUtil.createObjectNode(
 new Object() {{
 innerClassField = new NestedClass();
 }}
 );
 Object deserializedValue = new Object();
 JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
 JsonDeserializer<Class<?>> typeDeserializer = new StubJsonDeserializer();
 property.setValueDeserializer(deserializer);
 property.setValueSerializer(new StubJsonSerializer());
 property.setValueTypeDeserializer(typeDeserializer);
 property.setName("innerClassProperty");
 property.assignIndex(0);
 Object result = objectMapper.readValue(node.traverse(), Object.class);
 assertEquals("expected object with set innerClassProperty", result, bean);
 Object returnedValue = property.deserializeSetAndReturn(ObjectMapperTestUtil.createParser(node), null, bean);
 assertEquals("returned value should match deserialized value", deserializedValue, returnedValue);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object bean = new Object();
 Object newValue = new Object();
 Object result = property.setAndReturn(bean, newValue);
 assertEquals("returned value should match new value", newValue, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws Exception {
 property = new InnerClassProperty(
 new SettableBeanProperty(new BeanPropertyDefinition(
 Object.class.getDeclaredField("innerClassField"), null, null)), null);
 property.deserializeAndSet(ObjectMapperTestUtil.createParser(ObjectMapperTestUtil.createObjectNode(new Object())), null, new Object());
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testTransientConstructor() throws Exception {
 property = new InnerClassProperty(null, null);
 property.deserializeAndSet(ObjectMapperTestUtil.createParser(ObjectMapperTestUtil.createObjectNode(new Object())), null, new Object());
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testValueDeserializer() throws Exception {
 property.setValueDeserializer(null);
 ObjectNode node = ObjectMapperTestUtil.createObjectNode(
 new Object() {{
 innerClassField = "not an instance of NestedClass";
 }}
 );
 Object result = objectMapper.readValue(node.traverse(), Object.class);
 assertNull("result should be null when valueDeserializer is null", result);
 }

 @Test
 public void testValueSerializer() throws Exception {
 property.setValueSerializer(null);
 Object obj = new Object();
 obj.innerClassField = new NestedClass();
 ObjectNode node = objectMapper.writeValueAsObject(obj);
 assertTrue("node should not contain innerClassProperty", node.path("innerClassProperty").isMissingNode());
 }

 @Test
 public void testValueTypeDeserializer() throws Exception {
 property.setValueTypeDeserializer(null);
 ObjectNode node = ObjectMapperTestUtil.createObjectNode(
 new Object() {{
 innerClassField = new NestedClass();
 }}
 );
 ObjectMapper mapper = new ObjectMapper();
 Object deserializedValue = mapper.readValue(node.traverse(), Class.class);
 assertNull("deserialized value should be null when valueTypeDeserializer is null", deserializedValue);
 }

}

class NestedClass {
 // Nested class definition here
}

class StubJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return new Object();
 }
}

class StubJsonSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator jg, SerializerProvider provider) throws IOException {
 jg.writeNull();
 }
}