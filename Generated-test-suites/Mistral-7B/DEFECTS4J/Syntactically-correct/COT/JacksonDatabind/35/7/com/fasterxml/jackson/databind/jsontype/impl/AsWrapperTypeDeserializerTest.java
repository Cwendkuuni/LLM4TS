package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Tokener;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private TypeIdResolver idResolver;
 private AsWrapperTypeDeserializer deserializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 idResolver = mapper.getTypeIdResolver();
 deserializer = new AsWrapperTypeDeserializer(mapper.constructType(String.class), idResolver, "_typeId", true, String.class);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#AsWrapperTypeDeserializer(JavaType, TypeIdResolver, String, boolean, Class)}
 */
 @Test
 public void testAsWrapperTypeDeserializerConstructor() {
 // No validation needed for this method
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#AsWrapperTypeDeserializer(AsWrapperTypeDeserializer, BeanProperty)}
 */
 @Test
 public void testAsWrapperTypeDeserializerCopyConstructor() throws Exception {
 // No validation needed for this method

 AsWrapperTypeDeserializer copy = new AsWrapperTypeDeserializer(deserializer, null);
 assertEquals(deserializer, copy);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#forProperty(BeanProperty)}
 */
 @Test
 public void testForProperty() throws Exception {
 AsWrapperTypeDeserializer propDeserializer = new AsWrapperTypeDeserializer(deserializer, null);
 assertNotSame(deserializer, propDeserializer);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#getTypeInclusion()}
 */
 @Test
 public void testgetTypeInclusion() {
 assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromObject(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 String typeId = "MyType";
 String expected = "Hello World";

 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"" + typeId + "\", \"value\":\"" + expected + "\"}"));
 Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());

 assertEquals(expected, result);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromArray(JsonParser, DeserializationContext)}
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeTypedFromArray() throws Exception {
 deserializer.deserializeTypedFromArray(null, new TestContext());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromScalar(JsonParser, DeserializationContext)}
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeTypedFromScalar() throws Exception {
 deserializer.deserializeTypedFromScalar(null, new TestContext());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromAny(JsonParser, DeserializationContext)}
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeTypedFromAny() throws Exception {
 deserializer.deserializeTypedFromAny(null, new TestContext());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext)}
 */
 @Test(expected = IOException.class)
 public void test_deserializeWrongType() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"InvalidType\", \"value\":\"Hello World\"}"));
 deserializer._deserialize(parser, new TestContext());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext, Object)}
 */
 @Test
 public void test_deserialize() throws Exception {
 String typeId = "MyType";
 String expected = "Hello World";

 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"" + typeId + "\", \"value\":\"" + expected + "\"}"));
 ObjectResult result = (ObjectResult) mapper.readValue(parser, ObjectResult.class);
 Object deserialized = deserializer.deserialize(parser, new TestContext(), result);

 assertEquals(expected, deserialized);
 }

 /**
 * Test method for edge case where typeId is null
 */
 @Test(expected = IOException.class)
 public void test_deserializeNullTypeId() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{ \"value\":\"Hello World\"}"));
 deserializer._deserialize(parser, new TestContext());
 }

 /**
 * Test method for edge case where typeId is empty string
 */
 @Test(expected = IOException.class)
 public void test_deserializeEmptyTypeId() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"\", \"value\":\"Hello World\"}"));
 deserializer._deserialize(parser, new TestContext());
 }

 /**
 * Test method for edge case where value is null
 */
 @Test
 public void test_deserializeNullValue() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"MyType\", \"value\":null}"));
 Object result = deserializer._deserialize(parser, new TestContext());
 assertNull(result);
 }

 /**
 * Test method for edge case where value is empty string
 */
 @Test
 public void test_deserializeEmptyValue() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"MyType\", \"value\":\"\"}"));
 Object result = deserializer._deserialize(parser, new TestContext());
 assertNull(result);
 }

 /**
 * Test method for edge case where typeId is not a valid type name
 */
 @Test(expected = IOException.class)
 public void test_deserializeInvalidTypeId() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"InvalidType\", \"value\":\"Hello World\"}"));
 deserializer._deserialize(parser, new TestContext());
 }

 /**
 * Test method for edge case where typeId is not found
 */
 @Test(expected = IOException.class)
 public void test_deserializeNotFoundTypeId() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"typeId\":\"NonExistentType\", \"value\":\"Hello World\"}"));
 deserializer._deserialize(parser, new TestContext());
 }
}

 class TestContext extends DeserializationContext {
 @Override
 public DeserializationConfig getConfig() {
 return mapper.getConfiguration();
 }

 @Override
 public JavaType getRootValueType() {
 return null;
 }

 @Override
 public Object readValue(Class<?> type, JsonParser p) throws IOException {
 return mapper.readValue(p, type);
 }

 @Override
 public Object readValue(Class<?> type, JsonParser p, tokenStream.BaseTokenStream ts) throws IOException {
 return mapper.readerForUpdating(null).readValueAs(type, p, ts);
 }

 @Override
 public ObjectReader getRootValueFactory() {
 return mapper.getFactory().getValueFactory();
 }

 @Override
 public void reportError(MessageBase mb) throws IOException {
 // Do nothing
 }

 @Override
 public void reportMessage(MessageBase mb) throws IOException {
 // Do nothing
 }

 @Override
 public void handleUnexpectedValue(Object value, DeserializationContext ctxt) throws IOException {
 // Do nothing
 }

 @Override
 public void handleMissingValue(DeserializationContext ctxt) throws IOException {
 // Do nothing
 }

 @Override
 public void handleMissingProperty(DeserializationContext ctxt, BeanDescription beanDesc, String name) throws IOException {
 // Do nothing
 }

 @Override
 public void handleInstantiatingValue(Class<?> valueClass, DeserializationContext ctxt) throws IOException {
 // Do nothing
 }

 @Override
 public void handleTypeId(DeserializationContext ctxt, String tn, int id) throws IOException {
 // Do nothing
 }

 @Override
 public void handleUnknownProperty(DeserializationContext ctxt, BeanDescription beanDesc, String name, TokenId id) throws IOException {
 // Do nothing
 }
}