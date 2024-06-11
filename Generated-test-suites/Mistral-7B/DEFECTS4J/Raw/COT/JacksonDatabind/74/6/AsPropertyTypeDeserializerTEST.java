package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private AsPropertyTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 JavaType javaType = mapper().getTypeFactory().constructType(String.class);
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer deserializerType = objectMapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
 TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
 deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", true, deserializerType, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 ObjectTypeId id = new ObjectTypeId(String.class.getName());
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": {\"@type\": \"String\"}}");
 Object result = deserializer.deserializeTypedFromObject(parser, context());
 assertEquals("String", result.getClass().getName());
 assertEquals("String", parser.getText());
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": \"String\"}");
 Object result = deserializer.deserializeTypedFromObject(parser, context());
 assertEquals("String", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromObjectNullTypePropertyName() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{}");
 try {
 deserializer.deserializeTypedFromObject(parser, context());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("missing property 'typePropertyName' that is to contain type id  (for class java.lang.String)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeTypedFromObjectMissingStartObject() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": \"String\"}");
 parser.nextToken(); // consume field name
 try {
 deserializer.deserializeTypedFromObject(parser, context());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("missing START_OBJECT", e.getMessage());
 }
 }

 @Test
 public void testDeserializeTypedFromObjectMissingFieldName() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"@type\": \"String\"}");
 try {
 deserializer.deserializeTypedFromObject(parser, context());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("missing property 'typePropertyName' that is to contain type id  (for class java.lang.String)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": null}");
 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context(), null);
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithValidTypeId() throws Exception {
 ObjectTypeId id = new ObjectTypeId(String.class.getName());
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": {"+id.toString()+"}}");
 Object result = deserializer.deserializeTypedFromObject(parser, context());
 assertEquals("String", result.getClass().getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
 ObjectTypeId id = new ObjectTypeId("com.example.InvalidType.class");
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": {"+id.toString()+"}}");
 deserializer.deserializeTypedFromObject(parser, context());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromObjectWithTokenBuffer() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": \"String\"}");
 TokenBuffer tokenBuffer = new TokenBuffer(parser, context());
 Object result = deserializer.deserializeTypedForId(parser, context(), tokenBuffer);
 assertEquals("String", result.getClass().getName());
 assertEquals("String", parser.getText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObjectWithMissingTypePropertyName() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{}");
 Object result = deserializer.deserializeTypedForId(parser, context(), null);
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObjectWithUnmatchedTypePropertyName() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"invalidTypePropertyName\": \"String\"}");
 Object result = deserializer.deserializeTypedForId(parser, context(), null);
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": null}");
 Object result = deserializer.deserializeTypedForId(parser, context(), null);
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithMissingEndObject() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": \"String\"}");
 Object result = deserializer.deserializeTypedForId(parser, context(), null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromObjectWithEmptyJson() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("");
 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context(), null);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithInvalidJson() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("invalidJson");
 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context(), null);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithExtraFields() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"typePropertyName\": \"String\", \"extraField\": \"value\"}");
 Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context(), null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("[\"String\"]");
 Object result = deserializer.deserializeTypedFromArray(parser, context());
 assertArrayEquals(new String[]{"String"}, (Object[])result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromArrayWithNullArray() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("[]");
 Object result = deserializer.deserializeTypedFromArray(parser, context());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromArrayWithEmptyArray() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("[]");
 Object result = deserializer.deserializeTypedFromArray(parser, context());
 assertArrayEquals(new String[0], (Object[])result);
 }

 @Test
 public void testDeserializeTypedFromArrayWithNonArrayJson() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{ \"String\": \"Value\" }");
 Object result = deserializer.deserializeTypedFromArray(parser, context());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromScalar() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("\"String\"");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromScalarWithString() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("\"String\"");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 assertEquals("String", result.getClass().getName());
 assertEquals("String", parser.getText());
 }

 @Test
 public void testDeserializeTypedFromScalarWithInt() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("42");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 assertEquals(Integer.class, result.getClass());
 assertEquals(42, result.intValue());
 }

 @Test
 public void testDeserializeTypedFromScalarWithDouble() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("3.14159265");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 assertEquals(Double.class, result.getClass());
 assertEquals(3.14159265, result.doubleValue(), 0.0);
 }

 @Test
 public void testDeserializeTypedFromScalarWithBoolean() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("true");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 assertEquals(Boolean.class, result.getClass());
 assertEquals(true, result.booleanValue());
 }

 @Test
 public void testDeserializeTypedFromScalarWithBooleanFalse() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("false");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 assertEquals(Boolean.class, result.getClass());
 assertEquals(false, result.booleanValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromScalarWithInvalidJson() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("invalidJson");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromScalarWithExtraFields() throws Exception {
 JsonParser parser = mapper().getFactory().createParser("{\"extraField\": \"value\", \"String\": \"Value\"}");
 Object result = deserializer.deserializeTypedFromScalar(parser, context());
 fail("Expected JsonMappingException");
 }
}