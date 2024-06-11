package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class TypeDeserializerBaseTest {

    @Test
    public void test_findDeserializer_StringTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
    }

    @Test
    public void test_findDeserializer_NullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void test_findDeserializer_UnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.unknown.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(deserializer);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.unknown.MyClass";
        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNull(javaType);
    }

    @Test
    public void test_handleMissingTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createTestInstance();
        DeserializationContext ctxt = new TestDeserializationContext();
        JavaType javaType = typeDeserializerBase._handleMissingTypeId(ctxt, "");
        assertNull(javaType);
    }

    private TypeDeserializerBase createTestInstance() {
        TypeIdResolver idResolver = new TestTypeIdResolver();
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
                .use(new TestAnnotation())
                .build();
        return new TypeDeserializerBase(new TypeDescriptorBase<>(null, null, null, null, null, null), jsonTypeInfo, idResolver, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }

    private static class TestDeserializationContext extends DeserializationContext {
        public TestDeserializationContext() {
            super(null, null, null, null);
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }
    }

    private static class TestTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) {
            return null;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonSubTypes({
            @JsonSubTypes.With(value = MyClass.class, name = "MyClass")
    })
    static class MyClass {}

    static class TestAnnotation {
    }
}