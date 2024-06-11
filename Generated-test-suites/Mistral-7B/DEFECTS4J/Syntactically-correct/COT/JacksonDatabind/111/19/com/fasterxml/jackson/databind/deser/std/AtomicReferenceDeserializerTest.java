package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.StaticTypeDeserializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 // Test constructor
 @Test
 public void testAtomicReferenceDeserializerConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 assertNotNull(atomicReferenceDeserializer);
 }

 // Test getNullValue method
 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 AtomicReference<Object> result = deserializer.getNullValue(null);
 assertTrue(result instanceof AtomicReference);
 assertEquals(result.getClass(), AtomicReference.class);
 }

 // Test getEmptyValue method
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 AtomicReference<Object> result = deserializer.getEmptyValue(null);
 assertTrue(result instanceof AtomicReference);
 assertEquals(result.getClass(), AtomicReference.class);
 }

 // Test referenceValue method
 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertTrue(result instanceof AtomicReference);
 assertEquals(result.getClass(), AtomicReference.class);
 assertEquals(contents, result.get());
 }

 // Test getReferenced method
 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 Object result = deserializer.getReferenced(atomicReference);
 assertEquals(result, atomicReference.get());
 }

 // Test updateReference method
 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(atomicReference, newValue);
 assertEquals(updatedAtomicReference, atomicReference);
 assertEquals(newValue, atomicReference.get());
 }

 // Test supportsUpdate method
 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new ValueType(), new ValueInstantiator(), new StaticTypeDeserializer(AtomicReference.class), null);
 assertTrue(deserializer.supportsUpdate(null));
 }

 // Test empty constructor with instantiation
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyConstructorWithInstantiation() {
 new AtomicReferenceDeserializer(null, null, null, null);
 }

 // Test null value installer
 @Test(expected = IllegalArgumentException.class)
 public void testNullValueInstaller() {
 new AtomicReferenceDeserializer(new ValueType(), null, null, null);
 }

 // Test null typeDeserializer
 @Test(expected = IllegalArgumentException.class)
 public void testNullTypeDeserializer() {
 new AtomicReferenceDeserializer(new ValueType(), new ValueInstantiator(), null, null);
 }

 // Test null jsonDeserializer
 @Test(expected = IllegalArgumentException.class)
 public void testNullJsonDeserializer() {
 new AtomicReferenceDeserializer(new ValueType(), new ValueInstantiator(), new TypeDeserializer() {
 @Override
 public TypeDeserializer deserializeType(DeserializationContext ctxt, TypeDeserializer typeDeserializer, Object value, Type descriptor) throws JsonMappingException, JsonProcessingException {
 return new None();
 }
 }, null);
 }
}