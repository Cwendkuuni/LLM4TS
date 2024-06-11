package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testStdTypeResolverBuilder_NoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        TypeResolverBuilder<StdTypeResolverBuilder> actual = mapper.getTypeFactory()
                .constructType(TypeReference.getType(builder.getClass()));
        assertNull(actual);
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer actual = builder.buildTypeSerializer(mapper.getConfiguration(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertNull(actual);
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithCustomIdType() {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("typeIdProperty", "typePropertyName");
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typePropertyName");
        TypeSerializer actual = builder.buildTypeSerializer(mapper.getConfiguration(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertEquals("com.fasterxml.jackson.databind.ser.StdTypeSerializer$1",
                actual.getClass().getName());
        assertEquals("typePropertyName", actual.getPropertyName());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_Property() {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("typeIdProperty", "typePropertyName");
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typePropertyName");
        TypeDeserializer actual = builder.buildTypeDeserializer(mapper.getConfiguration(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertEquals("com.fasterxml.jackson.databind.deser.StdTypeDeserializer$1",
                actual.getClass().getName());
        assertEquals("typePropertyName", actual.getPropertyName());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_WrapperObject() {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("typeIdProperty", "typePropertyName");
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typePropertyName")
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeDeserializer actual = builder.buildTypeDeserializer(mapper.getConfiguration(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertEquals("com.fasterxml.jackson.databind.deser.WrapperTypeDeserializer$1",
                actual.getClass().getName());
        assertEquals("typePropertyName", actual.getPropertyName());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_WrapperArray() {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("typeIdProperty", "typePropertyName");
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typePropertyName")
                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer actual = builder.buildTypeDeserializer(mapper.getConfiguration(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertEquals("com.fasterxml.jackson.databind.deser.AsArrayTypeDeserializer$1",
                actual.getClass().getName());
        assertEquals("typePropertyName", actual.getPropertyName());
    }
}