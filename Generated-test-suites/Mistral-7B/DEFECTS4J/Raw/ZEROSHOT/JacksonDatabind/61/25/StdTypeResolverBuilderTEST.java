package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {

 private static class DummyClass {
 // Dummy class for testing
 }

 private JavaTypeFactory typeFactory;
 private ObjectMapper objectMapper;
 private StdTypeResolverBuilder builder;

 @Before
 public void setUp() {
 typeFactory = mock(JavaTypeFactory.class);
 objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector());
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testDefaultInitialization() {
 // Test default initialization
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
 assertNull(builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertFalse(builder.isTypeIdVisible());
 }

 @Test
 public void testNoTypeInfoBuilderInitialization() {
 // Test initialization via noTypeInfoBuilder()
 StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
 assertNull(builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertFalse(builder.isTypeIdVisible());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullIdTypeInitialization() {
 // Test initialization with null idType
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 }

 @Test
 public void testInitWithIdType() {
 // Test initialization with idType
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 builder.init(idType, idResolver);
 assertEquals(idType, builder.getIdType());
 assertEquals(idResolver, builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertFalse(builder.isTypeIdVisible());
 }

 @Test
 public void testBuildTypeSerializer() {
 // Test buildTypeSerializer
 Id idType = JsonTypeInfo.Id.PROPERTY;
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 JavaType baseType = JavaType.construct(DummyClass.class);
 Collection<NamedType> subtypes = Arrays.asList(
 new NamedType(Integer.class, "integerType"));
 TypeSerializer result = builder.init(idType, idResolver)
 .buildTypeSerializer(new SerializationConfig(objectMapper), baseType, subtypes);
 assertNull(result);

 idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
 result = builder.init(idType, idResolver)
 .buildTypeSerializer(new SerializationConfig(objectMapper), baseType, subtypes);
 assertNotNull(result);
 }

 @Test
 public void testBuildTypeDeserializer() {
 // Test buildTypeDeserializer
 Id idType = JsonTypeInfo.Id.PROPERTY;
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 JavaType baseType = JavaType.construct(DummyClass.class);
 Collection<NamedType> subtypes = Arrays.asList(
 new NamedType(Integer.class, "integerType"));
 TypeDeserializer result = builder.init(idType, idResolver)
 .buildTypeDeserializer(new DeserializationConfig(objectMapper), baseType, subtypes);
 assertNull(result);

 idType = JsonTypeInfo.Id.PROPERTY;
 result = builder.init(idType, idResolver)
 .inclusion(JsonTypeInfo.As.PROPERTY)
 .typeProperty("typeProperty")
 .buildTypeDeserializer(new DeserializationConfig(objectMapper), baseType, subtypes);
 assertNotNull(result);
 }

 @Test
 public void testGetDefaultImpl() {
 // Test getDefaultImpl
 Class<?> defaultImpl = String.class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder.getDefaultImpl());
 }

 @Test
 public void testTypeProperty() {
 // Test typeProperty
 String typeProperty = "typeProperty";
 builder.typeProperty(typeProperty);
 assertEquals(typeProperty, builder.getTypeProperty());
 }

 @Test
 public void testTypeIdVisibility() {
 // Test typeIdVisibility
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTypeIdResolver() {
 // Test initialization with null typeIdResolver
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 }

 @Test
 public void testIdResolver() {
 // Test idResolver
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 when(idResolver.idFromValue("testValue")).thenReturn(new TypeId("testType"));
 builder.init(JsonTypeInfo.Id.PROPERTY, idResolver);
 assertEquals("testType", builder.idResolver(new SerializationConfig(objectMapper), JavaType.construct(DummyClass.class), Arrays.asList(), true, false)
 .idFromValue("testValue"));
 }

 @Test
 public void testIdResolverForDeserialization() {
 // Test idResolver for deserialization
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 when(idResolver.idFromValue("testValue")).thenReturn(new TypeId("testType"));
 builder.init(JsonTypeInfo.Id.PROPERTY, idResolver);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(objectMapper), JavaType.construct(DummyClass.class), Arrays.asList())
 .forDeserialization();
 assertEquals("testType", deserializer.getTypeId());
 }

 @Test
 public void testIdResolverForSerialization() {
 // Test idResolver for serialization
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 when(idResolver.idFromValue("testValue")).thenReturn(new TypeId("testType"));
 builder.init(JsonTypeInfo.Id.PROPERTY, idResolver);
 TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(objectMapper), JavaType.construct(DummyClass.class), Arrays.asList())
 .forSerialization();
 assertEquals("testType", serializer.getTypeId());
 }

 @Test
 public void testGetIdType() {
 // Test getIdType
 Id idType = JsonTypeInfo.Id.CLASS;
 builder.init(idType, null);
 assertEquals(idType, builder.getIdType());
 }
}