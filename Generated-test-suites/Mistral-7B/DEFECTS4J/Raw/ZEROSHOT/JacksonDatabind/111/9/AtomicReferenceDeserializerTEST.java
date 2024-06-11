package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer atomicReferenceDeserializer;
 private DeserializationContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);

 JavaType fullType = mock(JavaType.class);
 ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
 TypeDeserializer typeDeserializer = new EmptyTypeDeserializer();
 JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

 atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
 }

 @Test
 public void testGetNullValue() throws IOException, JsonMappingException {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = atomicReferenceDeserializer.getNullValue(context);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = atomicReferenceDeserializer.getEmptyValue(context);
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = atomicReferenceDeserializer.referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = atomicReferenceDeserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(newValue);
 when(context.handleValueDeserialization(newValue, anyObject())).thenReturn(newValue);

 AtomicReference<Object> actual = atomicReferenceDeserializer.updateReference(reference, newValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testSupportsUpdate() throws IOException {
 assertTrue(atomicReferenceDeserializer.supportsUpdate(context.getDefaultConfig()));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = mapper.findBeanDescription("com.test.AtomicReferenceClass", null);
 NameTransformer nameTransformer = mapper.getPolymorphicTypeValidator().getNameTransformer();
 JavaType javaType = mapper.getTypeFactory().constructType(AtomicReference.class, Object.class);

 String jsonString = "{\"value\": \"test\"}";
 Object actual = mapper.readValue(jsonString, javaType);

 AtomicReference<Object> expected = new AtomicReference<Object>("test");
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected.get(), actual);
 }

}