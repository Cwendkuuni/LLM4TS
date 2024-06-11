package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeTypeDeserializer;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static final TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
 private static final JavaType baseType = new TypeReference<String>() {}.getType();

 @org.junit.BeforeClass
 public static void setup() {
 mapper.setTypeIdResolver(typeIdResolver);
 }

 @org.junit.Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "java.time.LocalDate");
 node.put("value", "2022-01-01");

 JsonParser parser = mapper.parserForObject(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());

 assertInstanceOf(JavaTimeTypeDeserializer.class, result);
 }

 @org.junit.Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "java.time.LocalDate");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("2022-01-01", "2022-01-01");
 node.put("value", valueNode);

 JsonParser parser = mapper.parserForObject(node);
 Object result = deserializer.deserializeTypedFromArray(parser, mapper.getDeserializationContext());

 assertInstanceOf(JavaTimeTypeDeserializer.class, result);
 }

 @org.junit.Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{\"typeId\":\"java.time.LocalDate\",\"value\":2022-01-01}");
 Object result = deserializer.deserializeTypedFromScalar(parser, mapper.getDeserializationContext());

 assertInstanceOf(JavaTimeTypeDeserializer.class, result);
 }

 @org.junit.Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{\"typeId\":\"java.time.LocalDate\"}");
 Object result = deserializer.deserializeTypedFromAny(parser, mapper.getDeserializationContext());

 assertInstanceOf(Object.class, result);
 }

 @org.junit.Test(expected = IOException.class)
 public void testInvalidJsonFormat() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{\"typeId\":\"java.time.LocalDate\"}");
 parser.nextToken(); // consume typeId
 parser.nextToken(); // consume :
 parser.nextToken(); // consume {
 parser.nextToken(); // consume type property
 parser.nextToken(); // consume :
 parser.nextToken(); // consume Int deserializer
 parser.nextToken(); // consume ,
 parser.nextToken(); // consume value
 parser.nextToken(); // consume :
 parser.nextToken(); // consume Int deserializer
 parser.nextToken(); // consume }
 parser.nextToken(); // consume unexpected "value"

 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 }

 @org.junit.Test(expected = IOException.class)
 public void testNullTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{ \"value\": 42 }");
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 }

 @org.junit.Test(expected = IOException.class)
 public void testEmptyTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{ \"typeId\": \"\", \"value\": 42 }");
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 }

 @org.junit.Test(expected = IOException.class)
 public void testInvalidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", false, String.class);

 JsonParser parser = mapper.parserForObject("{ \"typeId\": \"java.lang.Integer\", \"value\": 42 }");
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 }

 @org.junit.Test
 public void testMergeTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typeProperty", true, String.class);

 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "java.time.LocalDate");
 node.put("value", "2022-01-01");

 JsonParser parser = mapper.parserForObject(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());

 ObjectNode mergedNode = mapper.createObjectNode();
 mergedNode.put("_class", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer");
 mergedNode.put("_typeId", "java.time.LocalDate");
 mergedNode.put("value", result);
 assertEquals(mergedNode.toString(), mapper.writerForObject(mergedNode).writeValueAsString(result));
 }
}