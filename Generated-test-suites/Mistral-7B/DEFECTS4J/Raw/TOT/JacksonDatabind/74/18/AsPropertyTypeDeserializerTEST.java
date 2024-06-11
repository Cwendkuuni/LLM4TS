package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper mapper;
 private AsPropertyTypeDeserializer deserializer;

 @BeforeClass
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializePropertyTypeWithObject() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "{\"typeName\":\"MyClass\", \"field\":\"value\"}";
 Object result = deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 assertEquals(result.getClass(), MyClass.class);
 }

 @Test
 public void testDeserializePropertyTypeWithMissingTypeProperty() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "{\"field\":\"value\"}";
 assertThrows(JsonProcessingException.class, () -> {
 deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 });
 }

 @Test
 public void testDeserializePropertyTypeWithInvalidTypeProperty() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "{\"typeName\":\"InvalidType\", \"field\":\"value\"}";
 assertThrows(JsonMappingException.class, () -> {
 deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 });
 }

 @Test
 public void testDeserializePropertyTypeWithArray() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyArrayClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherArrayClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "[{\"typeName\":\"MyArrayClass\", \"elements\":[1,2,3]}]";
 Object result = deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 assertEquals(result.getClass(), MyArrayClass.class);
 }

 @Test
 public void testDeserializePropertyTypeWithArrayMissingTypeProperty() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyArrayClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherArrayClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "[[1,2,3]]";
 assertThrows(JsonProcessingException.class, () -> {
 deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 });
 }

 @Test
 public void testDeserializePropertyTypeWithArrayInvalidTypeProperty() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyArrayClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherArrayClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "[{\"typeName\":\"InvalidType\", \"elements\":[1,2,3]}]";
 assertThrows(JsonMappingException.class, () -> {
 deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 });
 }

 @Test
 public void testDeserializePropertyTypeWithNull() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "null";
 Object result = deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 assertNull(result);
 }

 @Test
 public void testDeserializePropertyTypeWithEmptyString() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "{}";
 assertThrows(JsonMappingException.class, () -> {
 deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 });
 }

 @Test
 public void testDeserializePropertyTypeWithSpaces() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
 String typePropertyName = "typeName";
 JavaType defaultImpl = mapper.getTypeFactory().constructType(MyOtherClass.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);

 String json = "{\" typeName\":\"MyClass\", \"field\":\"value\"}";
 Object result = deserializer.deserializeTypedFromObject(mapper.readTree(json), mapper.getContext());
 assertEquals(result.getClass(), MyClass.class);
 }
}