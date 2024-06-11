package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testStdTypeResolverBuilder_Default() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeResolver<StdTypeResolverBuilder> resolver = builder.build();

        TypeIdResolver typeIdResolver = resolver.getTypeIdResolver(mapper.getConfiguration(),
                JavaType.constructRawClassType(String.class), Collections.emptyList(), true, false);
        assertEquals(String.class, typeIdResolver.idFromValue("test"));
        assertNull(typeIdResolver.idFromValue(123));
    }

    @Test
    public void testStdTypeResolverBuilder_CustomIdResolver() {
        Id customIdType = Id.CUSTOM;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(customIdType, new MyCustomIdResolver());
        TypeResolver<StdTypeResolverBuilder> resolver = builder.build();

        TypeIdResolver typeIdResolver = resolver.getTypeIdResolver(mapper.getConfiguration(),
                JavaType.constructRawClassType(String.class), Collections.emptyList(), true, false);
        assertEquals(CustomId.class, typeIdResolver.idFromValue("test"));
        assertNull(typeIdResolver.idFromValue(123));
    }

    @Test
    public void testStdTypeResolverBuilder_CustomIdResolver_Class() {
        Id customIdType = Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(customIdType, new MyCustomIdResolver(String.class));
        TypeResolver<StdTypeResolverBuilder> resolver = builder.build();

        TypeIdResolver typeIdResolver = resolver.getTypeIdResolver(mapper.getConfiguration(),
                JavaType.constructRawClassType(String.class), Collections.emptyList(), true, false);
        assertEquals(String.class, typeIdResolver.idFromValue("test"));
        assertNull(typeIdResolver.idFromValue(123));
    }

    @Test
    public void testStdTypeResolverBuilder_Serialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.MINIMAL_CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("_type")
                .defaultImpl(String.class);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(),
                JavaType.constructRawClassType(CustomClass.class), Arrays.asList(
                        new NamedType(Integer.class, "int"),
                        new NamedType(String.class, "string")));

        CustomClass obj = new CustomClass();
        obj.setIntValue(123);
        obj.setStringValue("test");

        byte[] bytes = typeSerializer.serialize(obj, null);
        CustomClass deserialized = typeSerializer.deserialize(bytes, CustomClass.class);

        assertEquals(obj.getIntValue(), deserialized.getIntValue());
        assertEquals(obj.getStringValue(), deserialized.getStringValue());
    }

    @Test
    public void testStdTypeResolverBuilder_Deserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.MINIMAL_CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("_type")
                .defaultImpl(String.class);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(),
                JavaType.constructRawClassType(CustomClass.class), Arrays.asList(
                        new NamedType(Integer.class, "int"),
                        new NamedType(String.class, "string")));

        byte[] bytes = mapper.writeValueAsBytes(new CustomClass(123, "test"));
        CustomClass deserialized = typeDeserializer.deserialize(bytes, null);

        assertEquals(Integer.TYPE, deserialized.getClass().getComponentType());
        assertEquals(123, ((Number) deserialized).intValue());
        assertEquals("test", deserialized.getStringValue());
    }

    @After
    public void tearDown() {
        mapper = null;
    }
}

class CustomClass {
    private int intValue;
    private String stringValue;

    public CustomClass() {
    }

    public CustomClass(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}

class MyCustomIdResolver implements TypeIdResolver {
    private Class<?> idClass;

    public MyCustomIdResolver() {
        this(null);
    }

    public MyCustomIdResolver(Class<?> idClass) {
        this.idClass = idClass;
    }

    @Override
    public TypeId idFromValue(Object value) {
        if (value == null) {
            return null;
        }
        if (idClass != null && value.getClass().isAssignableFrom(idClass)) {
            return new TypeId(getClass().getName(), value.getClass());
        }
        return null;
    }

    @Override
    public Class<?> idFromString(String typeId) {
        try {
            return Class.forName(typeId);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown type id: " + typeId, e);
        }
    }
}

class CustomId extends TypeId {
    public CustomId(String name, Class<?> clazz) {
        super(name, clazz);
    }
}