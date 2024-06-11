package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private SimpleModule module;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 module = new SimpleModule();
 module.addDeserializer(AtomicReference.class, new AtomicReferenceDeserializer(TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null));
 module.addSerializer(AtomicReference.class, new StdSerializer<AtomicReference<Object>>(AtomicReference.class) {
 @Override
 public void serialize(AtomicReference<Object> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeObject(value.get());
 }
 });
 objectMapper.registerModule(module);
 }

 @Test
 public void testDeserialization() throws Exception {
 JsonNode node = JsonNodeFactory.instance.text("{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestReference\",\"value\":1}");
 JavaType type = TypeFactory.defaultInstance.constructType(AtomicReference.class, Integer.class);
 AtomicReference<Integer> reference = objectMapper.readValue(node, type);
 assertEquals(1, reference.get());
 }

 @Test
 public void testDeserializationWithNullValue() throws Exception {
 JsonNode node = JsonNodeFactory.instance.nullValue();
 AtomicReference<Integer> reference = objectMapper.readValue(node, AtomicReference.class);
 assertNull(reference);
 }

 @Test
 public void testDeserializationWithEmptyValue() throws Exception {
 JsonNode node = JsonNodeFactory.instance.objectNode();
 AtomicReference<Integer> reference = objectMapper.readValue(node, AtomicReference.class);
 assertNotNull(reference);
 assertEquals(null, reference.get());
 }

 @Test
 public void testDeserializationWithValue() throws Exception {
 JsonNode node = JsonNodeFactory.instance.text("{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestReference\",\"value\":2}");
 JavaType type = TypeFactory.defaultInstance.constructType(AtomicReference.class, Integer.class);
 AtomicReference<Integer> reference = objectMapper.readValue(node, type);
 assertEquals(2, reference.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidType() throws Exception {
 JsonNode node = JsonNodeFactory.instance.text("{\"class\":\"java.lang.String\",\"value\":2}");
 JavaType type = TypeFactory.defaultInstance.constructType(AtomicReference.class, Integer.class);
 AtomicReference<Integer> reference = null;
 try {
 reference = objectMapper.readValue(node, type);
 } catch (JsonMappingException e) {
 // expected
 }
 fail("Deserialization should have failed with JsonMappingException");
 }

 @Test
 public void testUpdate() throws Exception {
 AtomicReference<Integer> reference = new AtomicReference<Integer>(1);
 JavaType type = TypeFactory.defaultInstance.constructType(AtomicReference.class, Integer.class);
 JsonNode node = JsonNodeFactory.instance.text("{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestReference\",\"value\":3}");
 AtomicReference<Integer> updatedReference = objectMapper.readValue(node, type);
 updatedReference = (AtomicReference<Integer>) objectMapper.readerForUpdating(reference);
 updatedReference.set(updatedReference.get() + 1);
 assertEquals(2, reference.get());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullReference() throws Exception {
 AtomicReference<Integer> reference = null;
 AtomicReference<Integer> updatedReference = null;
 try {
 updatedReference = (AtomicReference<Integer>) objectMapper.readerForUpdating(reference);
 } catch (IllegalArgumentException e) {
 // expected
 }
 fail("Update should have failed with IllegalArgumentException");
 }

 @JsonSubTypes({
 @JsonSubTypes.With(name = "TestReference", value = AtomicReference.class)
 })
 static class TestReference extends AtomicReference<Object> {
 }
}