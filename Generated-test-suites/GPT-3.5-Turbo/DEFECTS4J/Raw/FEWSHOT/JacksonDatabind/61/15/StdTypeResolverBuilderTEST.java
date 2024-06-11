package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.init(JsonTypeInfo.Id.NAME, null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.typeProperty("typeIdPropName"));
    }
    
    @Test
    public void testTypePropertyWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.typeProperty(null));
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.defaultImpl(List.class));
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.typeIdVisibility(true));
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(builder, builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.buildTypeSerializer(null, null, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        builder.buildTypeSerializer(null, null, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.buildTypeDeserializer(null, null, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        builder.buildTypeDeserializer(null, null, null);
    }
    
    @Test
    public void testIdResolverWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.idResolver(null, null, null, true, true);
        // TODO: Add assertion
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
        builder.idResolver(null, null, null, true, true);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithUnknownIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        builder.idResolver(null, null, null, true, true);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.buildTypeDeserializer(null, null, null);
        // TODO: Add assertion
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.buildTypeSerializer(null, null, null);
        // TODO: Add assertion
    }
}