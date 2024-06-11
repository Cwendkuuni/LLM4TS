package com.example.junit.test.jackson.deserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 Assert.assertNull(new NullifyingDeserializer().deserialize(jsonParser, new TestContext()));
 }

 @Test
 public void testDeserializeWithType_InvalidToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), null));
 }

 @Test
 public void testDeserializeWithType_ValidObjectToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), null));
 }

 @Test
 public void testDeserializeWithType_ValidArrayToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), null));
 }

 @Test
 public void testDeserializeWithType_ValidFieldNameToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 JsonTokenId expectedToken = JsonTokenId.FIELD_NAME;
 jsonParser.nextToken();
 Assert.assertEquals(expectedToken, jsonParser.getCurrentTokenId());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithType_UnexpectedToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("true");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), null));
 }

 @Test
 public void testDeserializeWithType_ValidTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), typeDeserializer));
 }

 @Test
 public void testDeserializeWithType_NullTypeDeserializer() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 Assert.assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, new TestContext(), null));
 }
}

 class TestContext extends DeserializationContext {
 @Override
 public BeanDescription getStructDescription() {
 throw new UnsupportedOperationException();
 }

 @Override
 public BeanDescription findBeanDescription(Class<?> beanType, JsonDeserializer<?> deserializer) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void reportError(MessageBase msg) throws IOException {
 fail("Unexpected error: " + msg.toString());
 }

 @Override
 public void reportMessage(MessageBase msg) {
 // Intentionally left empty
 }

 @Override
 public void handleUnknownProperty(Object bean, String propertyName, JsonParser p) throws IOException {
 // Intentionally left empty
 }

 @Override
 public void handleMissingValue(Object bean, String propertyName) throws IOException {
 // Intentionally left empty
 }
}