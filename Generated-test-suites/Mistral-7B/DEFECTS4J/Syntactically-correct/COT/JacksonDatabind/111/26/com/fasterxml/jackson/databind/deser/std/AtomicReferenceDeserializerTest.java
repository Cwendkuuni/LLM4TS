package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testWithResolved() {
 TypeDeserializer typeDeser = mapper.getTypeDeserializer(AtomicReference.class);
 JsonDeserializer<?> valueDeser = mapper.deserializerOf(JsonSubType.Stable.unstableSerializer(String.class));
 AtomicReferenceDeserializer expected = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, typeDeser, valueDeser);
 AtomicReferenceDeserializer actual = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, typeDeser, valueDeser).withResolved(typeDeser, valueDeser);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 assertNotNull(deserializer.getNullValue(null));
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 assertEquals(new AtomicReference<Object>(), deserializer.getEmptyValue(null));
 }

 @Test
 public void testReferenceValue() {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 AtomicReference<Object> expected = new AtomicReference<Object>("test");
 AtomicReference<Object> actual = deserializer.referenceValue(mapper.readTree(node));
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetReferencedWithNullReference() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 AtomicReference<Object> reference = null;
 deserializer.getReferenced(reference);
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceWithNullReference() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 deserializer.updateReference(reference, null);
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(Type type) {
 return new AtomicReference<Object>();
 }
 }, null, null);
 assertTrue(deserializer.supportsUpdate(null));
 }
}