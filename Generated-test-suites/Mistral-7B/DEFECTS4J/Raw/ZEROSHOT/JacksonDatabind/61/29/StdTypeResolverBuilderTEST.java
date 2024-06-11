package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testEmptyBuilder() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> built = builder.build();
 assertNull(built);
 }

 @Test
 public void testNoTypeInfoBuilder() throws JsonProcessingException {
 StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> built = builder.build();
 assertNull(built);
 }

 @Test
 public void testBuilderWithDefaults() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 assertNotNull(serializer);
 }

 @Test
 public void testBuilderWithClassIdType() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeIdProp");
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.ClassIdResolver$StringSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithMinimalClassIdType() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.MinimalClassNameIdResolver$StringSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithNameType() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.NAME, null);
 JavaType baseType = JavaType.constructType("com.example.Person");
 Collection<NamedType> subtypes = Arrays.asList(
 new NamedType("com.example.Person.Child"),
 new NamedType("com.example.Person.Parent")
 );
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), baseType, subtypes);
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.TypeNameIdResolver$StringSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithWrapperArrayInclusion() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String[]"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.AsArrayTypeSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithPropertyInclusion() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 builder.typeProperty("typeIdProp");
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.AsPropertyTypeSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithWrapperObjectInclusion() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.AsWrapperTypeSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithExternalPropertyInclusion() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 builder.typeProperty("typeIdProp");
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.AsExternalTypeSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithExistingPropertyInclusion() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
 builder.typeProperty("typeIdProp");
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 TypeSerializer expected = mapper.getTypeFactory().constructType("com.fasterxml.jackson.databind.ser.std.AsPropertyTypeSerializer");
 assertEquals(expected.getClass(), serializer.getClass());
 }

 @Test
 public void testBuilderWithCustomIdResolver() throws Exception {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeIdResolver customResolver = mapper.getTypeFactory()
 .constructValueDeserializer(JavaType.valueOf("com.example.CustomTypeIdResolver"), MapperFactory.StdSerializers.EmptyValueSerializer.instance());
 builder.customIdResolver(customResolver);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.valueOf("java.lang.String"), Collections.emptyList());
 assertNotNull(serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuilderWithNullIdType() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuilderWithNullIdResolver() throws JsonProcessingException {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.customIdResolver(null);
 }
}