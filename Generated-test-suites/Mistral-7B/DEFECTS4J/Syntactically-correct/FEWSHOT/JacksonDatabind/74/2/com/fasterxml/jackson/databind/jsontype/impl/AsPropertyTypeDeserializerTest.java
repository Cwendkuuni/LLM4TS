package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
 String typePropertyName = "type";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createTestParser(), mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
 String typePropertyName = "type";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, true, mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createTestParser(), mapper.getDeserializationContext());
 assertEquals(0, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutProperty() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, null, false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createTestParser(), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
 JsonParser parser = createTestParser();
 parser.nextToken(); // token before "type"
 parser.setCurrentName("invalidTypeId");
 parser.nextToken(); // value
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(String.class), null, "type", true, null, As.PROPERTY);
 try {
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 JsonParser parser = createTestParser();
 parser.nextToken(); // token before "type"
 parser.setCurrentName("MyClass");
 parser.nextToken(); // value
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(String.class), null, "type", true, null, As.PROPERTY);
 assertEquals("MyClass", deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class);
 TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, null, false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromArray(createTestParser(), mapper.getDeserializationContext());
 assertEquals(new ArrayList<>(), result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromAny(createTestParser(), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAnyWithArray() throws Exception {
 JsonParser parser = mapper.createParser("[1, 2, 3]");
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
 assertEquals(Arrays.asList(1, 2, 3), deserializer.deserializeTypedFromAny(parser, mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromAnyWithObject() throws Exception {
 JsonParser parser = mapper.createParser("{\"type\": \"MyClass\", \"value\": \"test\"}");
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
 MyClass myClass = mapper.getTypeFactory().constructType(MyClass.class);
 assertEquals(myClass, deserializer.deserializeTypedFromAny(parser, mapper.getDeserializationContext()));
 }

 private JsonParser createTestParser() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"type\": \"String\", \"value\": \"test\"}");
 return parser;
 }

}