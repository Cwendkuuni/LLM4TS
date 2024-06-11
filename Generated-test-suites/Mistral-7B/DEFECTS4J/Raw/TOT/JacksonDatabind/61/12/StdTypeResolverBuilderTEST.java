package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setup() {
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(noTypeInfoBuilder.buildTypeSerializer(null, null, null));
 assertNull(noTypeInfoBuilder.buildTypeDeserializer(null, null, null));
 }

 @Test
 public void testInitWithNullIdType() {
 builder.init(null, null);
 try {
 builder.buildTypeSerializer(null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 builder.buildTypeDeserializer(null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithValidIdTypeAndIdResolver() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idResolver = null; // Will be set later
 builder.init(idType, idResolver);
 assertSame(idType, builder._idType);
 assertNull(builder._customIdResolver);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 @Test
 public void testBuildTypeSerializer_None() {
 builder.init(JsonTypeInfo.Id.NONE, null);
 assertNull(builder.buildTypeSerializer(null, null, null));
 }

 @Test
 public void testBuildTypeSerializer_Class() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 Class<?> baseType = MyClass.class;
 TypeSerializer result = builder.buildTypeSerializer(new MapperConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeSerializer_MinimalClassName() {
 String expectedTypeId = "Minimal";
 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 builder.typeProperty("typeId");
 Class<?> baseType = Minimal.class;
 TypeSerializer result = builder.buildTypeSerializer(new MapperConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeSerializer_Name() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.NAME, null);
 builder.typeProperty("typeId");
 Class<?> baseType = MyClass.class;
 TypeSerializer result = builder.buildTypeSerializer(new MapperConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeSerializer_Custom() {
 String expectedTypeId = "MyClass";
 TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
 when(customIdResolver.idFromValue(any())).thenReturn("MyClass");
 builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);
 builder.typeProperty("typeId");
 Class<?> baseType = MyClass.class;
 TypeSerializer result = builder.buildTypeSerializer(new MapperConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeSerializer_WrongIncludeAs() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 try {
 builder.buildTypeSerializer(null, null, null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testBuildTypeDeserializer_None() {
 builder.init(JsonTypeInfo.Id.NONE, null);
 assertNull(builder.buildTypeDeserializer(null, null, null));
 }

 @Test
 public void testBuildTypeDeserializer_Class() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_WrapperArray() {
 builder.init(JsonTypeInfo.As.WRAPPER_ARRAY, null);
 try {
 builder.buildTypeDeserializer(null, null, null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testBuildTypeDeserializer_Property() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_WrapperObject() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertNotNull(result);
 // Cannot test type id directly as it is not accessible
 }

 @Test
 public void testBuildTypeDeserializer_ExternalProperty() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getActiveTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_ExistingProperty() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_CustomIdResolver() {
 String expectedTypeId = "MyClass";
 TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
 when(customIdResolver.idFromValue(any())).thenReturn("MyClass");
 builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);
 builder.typeProperty("typeId");
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_DefaultImpl() {
 String expectedTypeId = "MyClass";
 Class<?> defaultImpl = MyDefaultImpl.class;
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.defaultImpl(defaultImpl);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testBuildTypeDeserializer_TypeIdVisible() {
 String expectedTypeId = "MyClass";
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typeId");
 builder.typeIdVisibility(true);
 Class<?> baseType = MyClass.class;
 TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, null);
 assertEquals(expectedTypeId, result.getTypeId());
 }

 @Test
 public void testIdResolver() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
 TypeIdResolver idResolver = builder.idResolver(new MapperConfig<>(), null, null, true, false);
 assertNotNull(idResolver);
 }
}