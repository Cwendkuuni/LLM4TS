package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AtomicReferenceDeserializerTest {

 private static final Object TEST_CONTENTS = new Object();

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer original = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 AtomicReferenceDeserializer updated = original.withResolved(new TypeDeserializer() {}, new JsonDeserializer<Object>() {});
 assertNotSame(original, updated);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 AtomicReference<Object> nullValue = deserializer.getNullValue(null);
 assertNull(nullValue.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 AtomicReference<Object> emptyValue = deserializer.getEmptyValue(null);
 assertNotNull(emptyValue);
 assertNull(emptyValue.get());
 }

 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 AtomicReference<Object> reference = deserializer.referenceValue(TEST_CONTENTS);
 assertNotNull(reference);
 assertEquals(TEST_CONTENTS, reference.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(TEST_CONTENTS);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 Object referenced = deserializer.getReferenced(reference);
 assertEquals(TEST_CONTENTS, referenced);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceNull() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 AtomicReference<Object> reference = new AtomicReference<Object>();
 try {
 deserializer.updateReference(reference, TEST_CONTENTS);
 fail("Should not be able to update null AtomicReference");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(TEST_CONTENTS);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 Object newContents = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
 assertEquals(newContents, updatedReference.get());
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 ObjectType.INSTANCE, new ValueInstantiator() {}, new TypeDeserializer() {}, null);
 assertTrue(deserializer.supportsUpdate(null));
 }
}