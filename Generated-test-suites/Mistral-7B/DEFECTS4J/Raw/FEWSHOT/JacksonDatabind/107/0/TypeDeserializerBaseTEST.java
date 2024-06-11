package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(
                Object.class.getTypeName(), new TypeIdResolver() {
                    @Override
                    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                        if ("MyClass".equals(typeId)) {
                            return MyClass.class.getTypeName();
                        }
                        return null;
                    }
                },
                "typePropertyName", true, MyClass.class);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "MyClass");
        assertNotNull(deserializer);
        assertEquals(MyClassDeserializer.class, deserializer.getClass());

        deserializer = typeDeserializer._findDeserializer(ctxt, "UnknownTypeId");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(
                Object.class.getTypeName(), new TypeIdResolver() {
                    @Override
                    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                        return null;
                    }
                },
                "typePropertyName", true, Void.TYPE);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.instance.getClass(), deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(
                Object.class.getTypeName(), new TypeIdResolver() {
                    @Override
                    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                        return null;
                    }
                },
                "typePropertyName", true, Void.TYPE);

        try {
            typeDeserializer._handleUnknownTypeId(ctxt, "UnknownTypeId");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(
                Object.class.getTypeName(), null, "typePropertyName", true, Void.TYPE);

        try {
            typeDeserializer._handleMissingTypeId(ctxt, "");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    static class MyClass {}
    static class MyClassDeserializer extends StdSerializer<MyClass> {}

    static class TestTypeDeserializerBase extends TypeDeserializerBase {
        TestTypeDeserializerBase(String baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    static class TestContext extends DeserializationContext {
        @Override
        public JavaType findBeanType(BeanProperty property) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(Class<?> type, PropertyName propertyName) {
            return null;
        }

        @Override
        public JavaType findValueDeserializer(Class<?> type, PropertyName propertyName) {
            return null;
        }

        @Override
        public JavaType handleUnknownTypeId(JavaType baseType, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType handleMissingTypeId(JavaType baseType, TypeIdResolver idResolver, String extraDesc) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}