package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeMapper;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 private AsPropertyTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 TypeFactory tf = mapper.getTypeFactory();
 JavaType javaType = tf.constructType(String.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(javaType, idResolver);
 deserializer = new AsPropertyTypeDeserializer(typeDeserializer, idResolver, "_typePropertyName", true, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyName() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("_typePropertyName", "StringType");
 node.put("value", "test");
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, getContext(parser));
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithEmptyJsonObject() throws IOException {
 JsonParser parser = mapper.createParser("{}");
 assertNull(deserializer.deserializeTypedFromObject(parser, getContext(parser)));
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("_typePropertyName", "StringType");
 node.put("typeId", "com.example.MyStringType");
 node.put("value", "test");
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, getContext(parser));
 TypeMapper typeMapper = mapper.getTypeFactory().constructType(String.class);
 assertEquals(typeMapper.constructType("com.example.MyStringType"), result.getClass());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws IOException {
 JsonParser parser = mapper.createParser("[]");
 Object result = deserializer.deserializeTypedFromObject(parser, getContext(parser));
 assertEquals(deserializer.getDefaultImpl().getType().getRawClass(), result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectMissingTypeProperty() throws IOException {
 JsonParser parser = mapper.createParser("{\"value\": \"test\"}");
 deserializer.deserializeTypedFromObject(parser, getContext(parser));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectInvalidTypePropertyName() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("_invalidTypePropertyName", "StringType");
 node.put("value", "test");
 JsonParser parser = mapper.treeAsTokens(node);
 deserializer.deserializeTypedFromObject(parser, getContext(parser));
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("_typePropertyName", "StringType");
 node.put("value", "[\"test1\", \"test2\"]");
 JsonParser parser = mapper.treeAsTokens(node);
 Object[] result = deserializer.deserializeTypedFromArray(parser, getContext(parser));
 assertEquals(2, result.length);
 assertEquals("test1", result[0]);
 assertEquals("test2", result[1]);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser parser = mapper.createParser("{\"value\": 123}");
 Object result = deserializer.deserializeTypedFromScalar(parser, getContext(parser));
 assertEquals(Integer.class, result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromScalarInvalidType() throws IOException {
 JsonParser parser = mapper.createParser("{\"value\": true}");
 deserializer.deserializeTypedFromScalar(parser, getContext(parser));
 }

 private DeserializationContext getContext(JsonParser jp) {
 return mapper.getDeserializationContext().withContextResolver(AsPropertyTypeDeserializer.class, new StaticContextResolver<AsPropertyTypeDeserializer>(deserializer));
 }

}