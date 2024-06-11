package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObject;
import org.jmock.lib.legacy.Mockery;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

 private static ObjectIdReader mockObjectIdReader;
 private static JsonDeserializer<ObjectIdNode> mockObjectIdNodeDeserializer;
 private static ObjectIdValueProperty objectIdValueProperty;
 private static JsonDeserializer<Object> mockIdPropertyDeserializer;

 @Before
 public void setUp() {
 mockObjectIdReader = mock(ObjectIdReader.class);
 ObjectIdValuePropertyTest.Class context = ClassImposteriser.getImposteriser().getInstance(Context.class);
 when(mockObjectIdReader.getIdType()).thenReturn(context.integerType);
 when(mockObjectObjectIdReader.getIdProperty()).thenReturn(mockIdPropertyDeserializer);
 mockObjectIdNodeDeserializer = mock(JsonDeserializer.class);
 ObjectIdValuePropertyTest.Context testContext = ObjectIdValuePropertyTest.Context.instance();
 when(testContext.findValueDeserializer(any(PropertyName.class), eq(Object.class))).thenReturn(mockObjectIdNodeDeserializer);
 ObjectIdValuePropertyTest.BeanProperty baseProperty = new ObjectIdValuePropertyTest.BeanProperty();
 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, baseProperty.metadata);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty property = objectIdValueProperty.withName(new PropertyName("test"));
 assertEquals("test", property.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty property = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 fail("Method should not be called for ObjectIdValueProperty");
 }

 @Test
 public void testGetMember() {
 fail("Method should not be called for ObjectIdValueProperty");
 }

 @Test
 public void testDeserializeAndSet() throws IOException, JsonProcessingException {
 Object id = new ObjectIdNode(1L);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsToken()).thenReturn(id.asToken());
 Object instance = new Object();
 Object readValue = new Object();
 objectIdValueProperty.deserializeAndSet(parser, mockContext(), instance);
 assertEquals(id.getId(), instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
 Object id = new ObjectIdNode(1L);
 Object instance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockParser(), mockContext(), instance);
 assertEquals(id.getId(), result);
 }

 @Test
 public void testSet() throws IOException, JsonProcessingException {
 Object id = new ObjectIdNode(1L).getId();
 Object instance = new Object();
 objectIdValueProperty.set(instance, id);
 assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException, JsonProcessingException {
 Object id = new ObjectIdNode(1L).getId();
 Object instance = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(id, result);
 }

 @Test
 public void testNullId() throws IOException, JsonProcessingException {
 Object instance = new Object();
 objectIdValueProperty.deserializeSetAndReturn(mockParser(), mockContext(), instance);
 assertNull(instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testIdPropertyIsNull() throws IOException, JsonProcessingException {
 Object id = new ObjectIdNode(1L).getId();
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 property.setAndReturn(instance, id);
 fail("Should throw UnsupportedOperationException");
 }

 @After
 public void tearDown() {
 mockery().assertIsSatisfied();
 }

 private JsonContext mockContext() {
 return new MockJsonContext();
 }

 private JsonParser mockParser() {
 return mock(JsonParser.class);
 }

 private static class MockJsonContext extends JsonContext {
 private static final long serialVersionUID = 1L;

 @Override
 public ObjectIdGenerator getObjectIdGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver getObjectIdResolver() {
 return null;
 }
 }

 private static class BeanProperty {
 PropertyMetadata metadata = new PropertyMetadata();
 }

 private static class Context {
 private static final long serialVersionUID = 1L;
 public final Class<Integer> integerType;

 private Object findValueDeserializer(PropertyName name, Class<?> cls) {
 return null;
 }
 }

 private static Mockery mockery() {
 return new MockObject(ObjectIdValuePropertyTest.class);
 }
}