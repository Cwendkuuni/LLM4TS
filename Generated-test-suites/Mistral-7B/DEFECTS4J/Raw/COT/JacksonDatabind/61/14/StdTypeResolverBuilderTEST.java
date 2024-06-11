package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setup() {
 builder = new StdTypeResolverBuilder();
 }

 // Test for constructor
 @Test
 public void testDefaultConstructor() {
 assertNotNull(builder);
 }

 // Test for noTypeInfoBuilder()
 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNotNull(noTypeInfoBuilder);
 }

 // Test for init method
 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = TypeIdResolver.identity();
 builder.init(idType, idRes);
 assertNotNull(builder._idType);
 assertNotNull(builder._customIdResolver);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 // Test for buildTypeSerializer method with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testBuildTypeSerializerWithNullArguments() {
 builder.buildTypeSerializer(null, null, null);
 }

 // Test for buildTypeSerializer method with valid arguments
 @Test
 public void testBuildTypeSerializer() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForValueType(String.class);
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"));
 TypeIdResolver idRes = TypeIdResolver.identity();
 TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(mapper), baseType, subtypes);
 assertNotNull(serializer);
 }

 // Test for buildTypeDeserializer method with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testBuildTypeDeserializerWithNullArguments() {
 builder.buildTypeDeserializer(null, null, null);
 }

 // Test for buildTypeDeserializer method with valid arguments
 @Test
 public void testBuildTypeDeserializer() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForValueType(String.class);
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"));
 TypeIdResolver idRes = TypeIdResolver.identity();
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(mapper), baseType, subtypes);
 assertNotNull(deserializer);
 }

 // Test for inclusion method with null argument
 @Test(expected = IllegalArgumentException.class)
 public void testInclusionWithNullArgument() {
 builder.inclusion(null);
 }

 // Test for inclusion method with valid argument
 @Test
 public void testInclusion() {
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertNotNull(builder._includeAs);
 }

 // Test for typeProperty method with null argument
 @Test
 public void testTypePropertyWithNullArgument() {
 builder.typeProperty(null);
 assertNotNull(builder._typeProperty);
 assertEquals(builder._typeProperty, JsonTypeInfo.Id.DEFAULT_PROPERTY_NAME);
 }

 // Test for typeProperty method with empty argument
 @Test
 public void testTypePropertyWithEmptyArgument() {
 builder.typeProperty("");
 assertNotNull(builder._typeProperty);
 assertEquals(builder._typeProperty, JsonTypeInfo.Id.DEFAULT_PROPERTY_NAME);
 }

 // Test for typeProperty method with valid argument
 @Test
 public void testTypeProperty() {
 builder.typeProperty("test");
 assertNotNull(builder._typeProperty);
 assertNotEquals(builder._typeProperty, JsonTypeInfo.Id.DEFAULT_PROPERTY_NAME);
 }

 // Test for defaultImpl method with null argument
 @Test
 public void testDefaultImplWithNullArgument() {
 builder.defaultImpl(null);
 assertNull(builder._defaultImpl);
 }

 // Test for defaultImpl method with valid argument
 @Test
 public void testDefaultImpl() {
 builder.defaultImpl(String.class);
 assertNotNull(builder._defaultImpl);
 assertSame(builder._defaultImpl, String.class);
 }

 // Test for typeIdVisibility method with invalid argument
 @Test(expected = IllegalArgumentException.class)
 public void testTypeIdVisibilityWithInvalidArgument() {
 builder.typeIdVisibility(-1);
 }

 // Test for typeIdVisibility method with valid argument
 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

 // Test for getDefaultImpl method
 @Test
 public void testGetDefaultImpl() {
 assertNull(builder.getDefaultImpl());
 }

 // Test for getTypeProperty method
 @Test
 public void testGetTypeProperty() {
 assertNull(builder.getTypeProperty());
 }

 // Test for isTypeIdVisible method
 @Test
 public void testIsTypeIdVisible() {
 assertFalse(builder.isTypeIdVisible());
 }

 // Test for idResolver method with null MapperConfig
 @Test(expected = IllegalArgumentException.class)
 public void testIdResolverWithNullMapperConfig() {
 builder.idResolver(null, null, null, true, false);
 }

 // Test for idResolver method with valid arguments
 @Test
 public void testIdResolver() {
 ObjectMapper mapper = new ObjectMapper();
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"));
 TypeIdResolver idRes = TypeIdResolver.identity();
 TypeIdResolver resolver = builder.idResolver(new MapperConfig(mapper), baseType, subtypes, true, false);
 assertNotNull(resolver);
 }
}