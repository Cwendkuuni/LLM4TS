package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testDefaultConstructor() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testInitWithIdTypeAndIdResolver() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());

 builder.init(JsonTypeInfo.Id.CLASS, new NoClassIdResolver());
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 assertNotNull(builder.getTypeIdResolver());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, new NoClassIdResolver());
 }

 @Test
 public void testBuildTypeSerializerWithNullConfig() {
 builder = new StdTypeResolverBuilder()
 .init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.buildTypeSerializer(null, null, null));
 }

 @Test
 public void testBuildTypeSerializerWithValidConfigAndBaseTypeButEmptySubtypes() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> emptySubtypes = JavaType.emptyCollection();
 TypeSerializer result = builder.buildTypeSerializer(config, baseType, emptySubtypes);
 assertNull(result);
 }

 @Test
 public void testBuildTypeSerializerWithValidConfigAndBaseTypeAndSubtypes() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, String.class);
 TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithInvalidInclusionType() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, String.class);
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.NONE, null);
 builder.buildTypeSerializer(config, baseType, subtypes);
 }

 @Test
 public void testBuildTypeDeserializerWithNullConfig() {
 builder = new StdTypeResolverBuilder()
 .init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.buildTypeDeserializer(null, null, null));
 }

 @Test
 public void testBuildTypeDeserializerWithValidConfigAndBaseTypeButEmptySubtypes() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> emptySubtypes = JavaType.emptyCollection();
 TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, emptySubtypes);
 assertNull(result);
 }

 @Test
 public void testBuildTypeDeserializerWithValidConfigAndBaseTypeAndSubtypes() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, String.class);
 TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithInvalidInclusionType() {
 MapperConfig config = new MapperConfig(new AnnotationIntrospector());
 JavaType baseType = config.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, String.class);
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.NONE, null);
 builder.buildTypeDeserializer(config, baseType, subtypes);
 }

 @Test
 public void testInclusion() {
 builder = new StdTypeResolverBuilder();
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusionWithNullIncludeAs() {
 builder = new StdTypeResolverBuilder();
 builder.inclusion(null);
 }

 @Test
 public void testTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.typeProperty("testProperty");
 assertEquals("testProperty", builder.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testGetDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testGetTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.typeProperty("testProperty");
 assertEquals("testProperty", builder.getTypeProperty());
 }

 @Test
 public void testIsTypeIdVisible() {
 builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }
}