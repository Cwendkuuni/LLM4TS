package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonSubTypes;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private StandardDeserializationContext context;
 private ValueInstantiator valueInstantiator;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<AtomicReference<Object>> deserializer;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 context = new StandardDeserializationContext(objectMapper.getConfig(), null);
 valueInstantiator = new StdValueInstantiator();
 typeDeserializer = new BaseTypeDeserializer() {
 @Override
 public JsonDeserializer<?> createContextual(DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
 return null;
 }
 };
 deserializer = new AtomicReferenceDeserializer(
 ObjectTypeFactories.anyUnwrappedType(AtomicReference.class, Object.class), valueInstantiator, typeDeserializer, null);
 }

 // Test methods for public constructors and static fields

 @Test
 public void testAtomicReferenceDeserializerConstructor() {
 AtomicReferenceDeserializer instance = new AtomicReferenceDeserializer(null, null, null, null);
 assertNotNull(instance);
 }

 // Test method for getNullValue()

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> actualNullValue = deserializer.getNullValue(context);
 assertTrue(actualNullValue instanceof AtomicReference);
 assertNull(actualNullValue.get());
 }

 @Test
 public void testGetNullValueWithValueDeserializer() throws JsonMappingException {
 AtomicReference<Object> actualNullValue = deserializer.getNullValue(context);
 AtomicReference<String> nullValueWithValueDeserializer = new AtomicReference<String>("test");
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 AtomicReference<Object> actualNullValueWithValueDeserializer = deserializer.getNullValue(context, stringDeserializer);
 assertEquals(actualNullValue, actualNullValueWithValueDeserializer);
 }

 // Test method for getEmptyValue()

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> actualEmptyValue = deserializer.getEmptyValue(context);
 assertTrue(actualEmptyValue instanceof AtomicReference);
 assertNull(actualEmptyValue.get());
 }

 // Test method for referenceValue()

 @Test
 public void testReferenceValue() {
 ObjectNode source = ObjectMapperFactory.getObjectMapper().createObjectNode();
 source.put("value", "test");
 AtomicReference<Object> expectedValue = new AtomicReference<Object>("test");
 AtomicReference<Object> actualValue = deserializer.referenceValue(source.toString());
 assertEquals(expectedValue, actualValue);
 }

 // Test method for getReferenced()

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 Object actualValue = deserializer.getReferenced(reference);
 assertEquals("test", actualValue);
 }

 // Test method for updateReference()

 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 AtomicReference<String> newValue = new AtomicReference<String>("newTest");
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 assertEquals(newValue, updatedReference);
 assertEquals("newTest", updatedReference.get());
 }

 // Test method for supportsUpdate()

 @Test
 public void testSupportsUpdate() {
 assertTrue(deserializer.supportsUpdate(context.getDefaultConfig()));
 }

 // Test edge cases and exception handling

 @Test(expected = JsonMappingException.class)
 public void testGetNullValueWithInvalidType() throws JsonMappingException {
 JsonDeserializer<Integer> intDeserializer = new IntDeserializer();
 deserializer.getNullValue(context, intDeserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testReferenceValueWithInvalidJson() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode source = objectMapper.createObjectNode();
 source.put("invalidJson", "test");
 deserializer.referenceValue(source.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateReferenceWithNullReference() {
 AtomicReference<Object> reference = null;
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, new AtomicReference<Object>("test"));
 fail("IllegalStateException expected but not thrown");
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateReferenceWithNullNewValue() {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, null);
 fail("IllegalStateException expected but not thrown");
 }
}