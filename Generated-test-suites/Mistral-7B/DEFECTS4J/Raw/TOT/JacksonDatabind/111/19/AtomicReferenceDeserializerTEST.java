package com.example.junit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

 private static ObjectMapper mapper;

 static {
 mapper = new ObjectMapper();
 }

 @org.junit.Test
 public void testDeserializeNullAtomicReference() throws IOException {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(new TypeReference<AtomicReference<Object>>() {}), null, null, null);
 AtomicReference<Object> actual = deserializer.deserialize(mapper.getFactory().getNullValue(), new DeserializationContext(mapper.getFactory(), null, null));
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testDeserializeEmptyAtomicReference() throws IOException {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(new TypeReference<AtomicReference<Object>>() {}), null, null, null);
 String json = "{}";
 ObjectReadContext context = mapper.readerForValueType(AtomicReference.class).readValueAs(String.class, json);
 AtomicReference<Object> actual = deserializer.deserialize(context, new DeserializationContext(mapper.getFactory(), null, null));
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testDeserializeAtomicReferenceWithValue() throws IOException {
 AtomicReference<Integer> expected = new AtomicReference<>(1);
 ObjectReadContext context = mapper.readerForValueType(new TypeReference<AtomicReference<Integer>>() {}).readValue(new ObjectMapper(), "{\"@type\":\"com.fasterxml.jackson.databind.AtomicReference\",\"value\":1}");
 AtomicReference<Object> actual = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(new TypeReference<AtomicReference<Object>>() {}), null, null, null).deserialize(context, new DeserializationContext(mapper.getFactory(), null, null));
 assertEquals(expected, actual);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testDeserializeAtomicReferenceWithInvalidType() throws IOException {
 ObjectReadContext context = mapper.readerForValueType(mapper.getTypeFactory().constructType(Integer.class)).readValue(new ObjectMapper(), "{\"@type\":\"com.fasterxml.jackson.databind.AtomicReference\",\"value\":1}");
 }

 @org.junit.Test
 public void testUpdateAtomicReference() throws IOException {
 AtomicReference<Integer> expected = new AtomicReference<>(1);
 AtomicReference<Integer> reference = new AtomicReference<>(1);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(new TypeReference<AtomicReference<Integer>>() {}), null, null, null);
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.AtomicReference\",\"value\":2}";
 ObjectReadContext context = mapper.readerForValueType(new TypeReference<AtomicReference<Integer>>() {}).readValue(new ObjectMapper(), json);
 AtomicReference<Object> actual = deserializer.updateReference(reference, context.getValueAs(AtomicReference.class));
 assertEquals(expected, actual);
 }
}