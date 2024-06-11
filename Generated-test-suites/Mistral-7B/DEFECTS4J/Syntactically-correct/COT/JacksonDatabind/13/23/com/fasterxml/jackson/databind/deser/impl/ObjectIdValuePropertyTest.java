package com.fasterxml.jackson.databind.deser.impl.test;

 import static org.junit.Assert.*;

 import com.fasterxml.jackson.core.JsonParser;
 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.databind.*;
 import com.fasterxml.jackson.databind.deser.*;
 import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
 import com.fasterxml.jackson.databind.node.ObjectIdReader;
 import com.fasterxml.jackson.databind.ser.std.StdSerializer;
 import com.fasterxml.jackson.databind.util.BeanUtil;

 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;

 public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<String> deserializer;
 private Object testInstance;

 @Before
 public void setUp() throws Exception {
 objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdGenerator getObjectIdGenerator() {
 return new ObjectIdGenerator() {};
 }

 @Override
 public ObjectIdResolver getObjectIdResolver() {
 return new ObjectIdResolver() {};
 }

 @Override
 public PropertyName getPropertyName() {
 return new PropertyName("testProperty");
 }

 @Override
 public Class<?> getIdType() {
 return String.class;
 }
 };

 deserializer = new StdSerializer<String>(String.class) {};
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 testInstance = new Object() {};
 }

 @After
 public void tearDown() throws Exception {
 // Clear any side effects that may have occurred during testing
 BeanUtil.setSimpleValue(testInstance, objectIdValueProperty.getName(), null);
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(ObjectIdValueProperty.class, actualObjectIdValueProperty.getClass());
 assertEquals(objectIdReader.propertyName, actualObjectIdValueProperty.getName());
 assertEquals(objectIdReader.getIdType(), actualObjectIdValueProperty.getType());
 assertEquals(objectIdReader, actualObjectIdValueProperty._objectIdReader);
 assertEquals(null, actualObjectIdValueProperty.getDeserializer());
 }

 @Test
 public void testConstructorWithDeserializer() {
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, deserializer);
 assertEquals(ObjectIdValueProperty.class, actualObjectIdValueProperty.getClass());
 assertEquals(objectIdReader.propertyName, actualObjectIdValueProperty.getName());
 assertEquals(objectIdReader.getIdType(), actualObjectIdValueProperty.getType());
 assertEquals(objectIdReader, actualObjectIdValueProperty._objectIdReader);
 assertEquals(deserializer, actualObjectIdValueProperty.getDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testConstructorDeprecated() throws Exception {
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("testNewName"));
 fail("Should not reach here");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testConstructorDeprecatedWithString() throws Exception {
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, "testNewName");
 fail("Should not reach here");
 }

 @Test
 public void testWithName() throws Exception {
 ObjectIdValueProperty actualObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("testNewName"));
 assertEquals(new PropertyName("testNewName"), actualObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 ObjectIdValueProperty actualObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, actualObjectIdValueProperty.getDeserializer());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String testId = "testId";
 JsonParser jp = new JsonFactory().createParser("{\"id\":\"" + testId + "\"}");
 ObjectIdValueProperty.Deserializer deserializer = (ObjectIdValueProperty.Deserializer) objectIdValueProperty.getDeserializer();
 deserializer.deserializeAndSet(jp, new TestContext(), testInstance);
 assertEquals(testId, BeanUtil.getSimpleValue(testInstance, objectIdValueProperty.getName()));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 String testId = "testId";
 Object id = deserializer.deserialize(new JsonFactory().createParser("{\"id\":\"" + testId + "\"}"), new TestContext());
 Object objectId = objectIdValueProperty.deserializeSetAndReturn(new JsonParser(new StringReader("{\"id\":\"" + testId + "\"}")), new TestContext(), testInstance);
 assertSame(testInstance, objectId);
 assertEquals(testId, BeanUtil.getSimpleValue(testInstance, objectIdValueProperty.getName()));
 }

 @Test
 public void testSet() throws Exception {
 String testId = "testId";
 BeanUtil.setSimpleValue(testInstance, objectIdValueProperty.getName(), testId);
 assertEquals(testId, BeanUtil.getSimpleValue(testInstance, objectIdValueProperty.getName()));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 String testId = "testId";
 Object oldValue = BeanUtil.getSimpleValue(testInstance, objectIdValueProperty.getName());
 Object newValue = new Object() {};
 Object result = objectIdValueProperty.setAndReturn(testInstance, newValue);
 assertEquals(newValue, result);
 assertNotEquals(oldValue, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithNullInstance() throws Exception {
 ObjectIdValueProperty.Setter setter = (ObjectIdValueProperty.Setter) objectIdValueProperty.getSetter();
 setter.setAndReturn(null, "testValue");
 fail("Should not reach here");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithNullValue() throws Exception {
 ObjectIdValueProperty.Setter setter = (ObjectIdValueProperty.Setter) objectIdValueProperty.getSetter();
 Object result = setter.setAndReturn(testInstance, null);
 fail("Should not reach here");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullValue() throws Exception {
 JsonParser jp = new JsonFactory().createParser("{}");
 ObjectIdValueProperty.Deserializer deserializer = (ObjectIdValueProperty.Deserializer) objectIdValueProperty.getDeserializer();
 deserializer.deserializeAndSet(jp, new TestContext(), testInstance);
 fail("Should throw IOException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeSetAndReturnWithNullValue() throws Exception {
 Object id = null;
 Object objectId = objectIdValueProperty.deserializeSetAndReturn(new JsonParser(new StringReader("{\"id\":null}")), new TestContext(), testInstance);
 fail("Should throw JsonMappingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetWithInvalidJson() throws Exception {
 JsonParser jp = new JsonFactory().createParser("{\"invalidJson\":\"testId\"}");
 ObjectIdValueProperty.Deserializer deserializer = (ObjectIdValueProperty.Deserializer) objectIdValueProperty.getDeserializer();
 deserializer.deserializeAndSet(jp, new TestContext(), testInstance);
 fail("Should throw JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeSetAndReturnWithInvalidJsonType() throws Exception {
 JsonParser jp = new JsonFactory().createParser("{\"id\":[1, 2, 3]}");
 ObjectIdValueProperty.Deserializer deserializer = (ObjectIdValueProperty.Deserializer) objectIdValueProperty.getDeserializer();
 deserializer.deserializeSetAndReturn(jp, new TestContext(), testInstance);
 fail("Should throw JsonProcessingException");
 }

 @Test
 public void testDeserializeSetAndReturnWithEmptyJson() throws Exception {
 Object objectId = objectIdValueProperty.deserializeSetAndReturn(new JsonParser(new StringReader("{}")), new TestContext(), testInstance);
 assertSame(testInstance, objectId);
 }

 }