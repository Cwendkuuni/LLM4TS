package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonTest;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest extends JsonTest {

 private static final AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer actual = new AtomicReferenceDeserializer(null, null, null, null);
 assertInstanceOf(AtomicReferenceDeserializer.class, actual);
 }

 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer actual = serializer.withResolved(null, null);
 assertInstanceOf(AtomicReferenceDeserializer.class, actual);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = serializer.getNullValue(createContext());
 assertSame(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> actual = serializer.getEmptyValue(createContext());
 assertInstanceOf(AtomicReference.class, actual);
 assertNull(actual.get());
 }

 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> actual = serializer.referenceValue(contents);
 assertInstanceOf(AtomicReference.class, actual);
 assertSame(contents, actual.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = serializer.getReferenced(reference);
 assertSame(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> actual = serializer.updateReference(reference, newValue);
 assertSame(reference, actual);
 assertSame(newValue, reference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSupportsUpdateThrowsExceptionWhenFalse() {
 assertFalse(serializer.supportsUpdate(createContext()));
 }

 @Test
 public void testSupportsUpdateReturnsTrue() {
 assertTrue(serializer.supportsUpdate(createContext()));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetReferencedThrowsExceptionWhenNullReference() {
 serializer.getReferenced(null);
 }

 @Test
 public void testUpdateReferenceWithNullReference() {
 AtomicReference<Object> reference = null;
 Object newValue = new Object();
 AtomicReference<Object> actual = serializer.updateReference(reference, newValue);
 assertNull(actual);
 }

 @Test
 public void testUpdateReferenceWithNullNewValue() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = null;
 AtomicReference<Object> actual = serializer.updateReference(reference, newValue);
 assertSame(reference, actual);
 assertSame(reference.get(), actual.get());
 }

}