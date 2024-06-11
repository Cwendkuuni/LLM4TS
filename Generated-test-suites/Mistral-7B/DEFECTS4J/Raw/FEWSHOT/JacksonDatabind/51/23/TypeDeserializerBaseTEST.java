package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWhenTypeIdIsPresent() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolver(), "", false, ClassUtil.newSimpleType(String.class));
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "testTypeId");
        assertNotNull(deserializer);
        assertEquals("expected deserializer", StringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerWhenTypeIdIsAbsent() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolver(), "", false, ClassUtil.newSimpleType(String.class));
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWhenDefaultImplIsPresent() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolver(), "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals("expected deserializer", NullifyingDeserializer.instance.getClass(), deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerWhenDefaultImplIsAbsent() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolver(), "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void test_handleUnknownTypeIdWhenTypeIdResolverReturnsNull() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolverThatReturnsNull(), "", false, null);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", new DummyTypeIdResolver(), null);
        assertNull(result);
    }

    @Test
    public void test_handleUnknownTypeIdWhenTypeIdResolverReturnsType() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(ClassUtil.newSimpleType(String.class),
                new DummyTypeIdResolverThatReturnsType(), "", false, null);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", new DummyTypeIdResolverThatReturnsType(), null);
        assertNotNull(result);
        assertEquals("expected type", ClassUtil.newSimpleType(Integer.class), result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[0];
        }

        @Override
        public JavaType typeFromId(String name, TypeIdResolver resolver) throws IOException {
            return resolver.typeFromId(this, name);
        }
    }

    private static class DummyTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String name) throws IOException {
            if ("testTypeId".equals(name)) {
                return ClassUtil.newSimpleType(String.class);
            }
            return null;
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }
    }

    private static class DummyTypeIdResolverThatReturnsNull extends DummyTypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String name) throws IOException {
            return null;
        }
    }

    private static class DummyTypeIdResolverThatReturnsType extends DummyTypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String name) throws IOException {
            return ClassUtil.newSimpleType(Integer.class);
        }
    }

    private static class StringDeserializer extends JsonDeserializer<String> {
        static final StringDeserializer instance = new StringDeserializer();

        private StringDeserializer() {
            // empty constructor
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    private static abstract class TestTypeDeserializerBase extends TypeDeserializerBase {
        TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            throw new UnsupportedOperationException("Should be overridden in subclasses");
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            throw new UnsupportedOperationException("Should be overridden in subclasses");
        }
    }
}