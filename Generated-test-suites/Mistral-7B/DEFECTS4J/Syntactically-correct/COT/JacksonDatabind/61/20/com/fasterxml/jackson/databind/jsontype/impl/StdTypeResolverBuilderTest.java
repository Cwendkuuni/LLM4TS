package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testConstructor() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testInitMethod() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());

 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());

 builder.init(JsonTypeInfo.Id.NAME, null);
 assertEquals(JsonTypeInfo.Id.NAME, builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());

 builder.init(JsonTypeInfo.Id.CUSTOM, null);
 assertEquals(JsonTypeInfo.Id.CUSTOM, builder.getIdType());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 }

 @Test
 public void testBuildTypeSerializer() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 // Then
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWithNullBaseType() {
 // Given
 MapperConfig config = new MapperConfig();
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeSerializer serializer = builder.buildTypeSerializer(config, null, subtypes);
 // Then
 assertNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWithEmptySubtypes() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 // Then
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWithInvalidIncludeAs() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When, Then
 assertNull(builder.buildTypeSerializer(config, baseType, subtypes));
 }

 @Test
 public void testBuildTypeDeserializer() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 // Then
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWithNullBaseType() {
 // Given
 MapperConfig config = new MapperConfig();
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, null, subtypes);
 // Then
 assertNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWithEmptySubtypes() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 // Then
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWithInvalidIncludeAs() {
 // Given
 MapperConfig config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 // When, Then
 assertNull(builder.buildTypeDeserializer(config, baseType, subtypes));
 }

 @Test
 public void testTypePropertyMethod() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.typeProperty("test");
 // Then
 assertEquals("test", builder.getTypeProperty());
 }

 @Test
 public void testTypePropertyWithEmptyString() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.typeProperty("");
 // Then
 assertNull(builder.getTypeProperty());
 }

 @Test
 public void testDefaultImplMethod() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.defaultImpl(Object.class);
 // Then
 assertEquals(Object.class, builder.getDefaultImpl());
 }

 @Test
 public void testDefaultImplWithNullClass() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.defaultImpl(null);
 // Then
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibilityMethod() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.typeIdVisibility(true);
 // Then
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testTypeIdVisibilityMethodWithFalse() {
 // Given
 builder = new StdTypeResolverBuilder();
 // When
 builder.typeIdVisibility(false);
 // Then
 assertFalse(builder.isTypeIdVisible());
 }
}