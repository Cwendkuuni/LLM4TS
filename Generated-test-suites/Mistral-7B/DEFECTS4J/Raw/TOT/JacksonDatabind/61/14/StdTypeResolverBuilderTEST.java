package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.json.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new MapperFactoryBuilder()
 .builder()
 .initialize(new StdTypeResolverBuilder()
 .include(As.WRAPPER_OBJECT)
 .typeProperty("type"))
 .build());
 }

 @After
 public void cleanup() {
 objectMapper = null;
 }

 @Test
 public void testBuildTypeDeserializerWithValidInput() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 Class<String> stringClass = String.class;
 NamedType namedType = new NamedType(String.class, "StringType");
 NamedType namedType2 = new NamedType(Integer.class, "IntegerType");
 Collection<NamedType> subtypes = java.util.Arrays.asList(namedType, namedType2);

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, subtypes);
 assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithNullIdType() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(null, idResolver)
 .typeProperty("type");
 builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithNullIdResolver() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = null;

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithNullBaseType() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 JavaType baseType = null;
 Collection<NamedType> subtypes = java.util.Arrays.asList(new NamedType(String.class, "StringType"));

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, subtypes);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithNullSubtypes() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 Collection<NamedType> subtypes = null;

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, subtypes);
 }

 @Test
 public void testBuildTypeSerializerWithValidInput() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
 assertEquals(TypeSerializer.class, typeSerializer.getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullIdType() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(null, idResolver)
 .typeProperty("type");
 builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullIdResolver() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = null;

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullBaseType() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 TypeIdResolver idResolver = objectMapper.getTypeFactory().getPolymorphicTypeIdResolver();

 JavaType baseType = null;

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CLASS, idResolver)
 .typeProperty("type");
 builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
 }

 @Test
 public void testBuildTypeDeserializerWithCustomIdResolver() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver customIdResolver = new CustomTypeIdResolver();

 Collection<NamedType> subtypes = java.util.Arrays.asList(new NamedType(String.class, "StringType"));

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CUSTOM, customIdResolver)
 .typeProperty("type");
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, subtypes);
 assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
 }

 @Test
 public void testBuildTypeSerializerWithCustomIdResolver() throws Exception {
 TypeReference<JavaType> javaTypeTypeRef = new TypeReference<JavaType>() {};
 JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
 TypeIdResolver customIdResolver = new CustomTypeIdResolver();

 StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
 .init(Id.CUSTOM, customIdResolver)
 .typeProperty("type");
 TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
 assertEquals(TypeSerializer.class, typeSerializer.getClass());
 }
}

class CustomTypeIdResolver implements TypeIdResolver {
 @Override
 public String idFromValue(Object value) {
 return "CustomTypeId";
 }

 @Override
 public JavaType idToValue(String id) {
 throw new UnsupportedOperationException();
 }
}