package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typeId = "com.example.Foo";
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(null, idResolver, "typePropertyName", true, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(new TestContext(), typeId);
        assertNotNull(deserializer);
        assertEquals(MyDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(null, idResolver, "typePropertyName", true, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(new TestContext(), null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typeId = "com.unknown.UnknownClass";
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(null, idResolver, "typePropertyName", true, null);
        try {
            JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(new TestContext(), typeId);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(null, idResolver, "typePropertyName", true, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(new TestContext());
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(new MyClassType(MyOtherClass.class), idResolver, "typePropertyName", true, MyOtherClass.class);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(new TestContext());
        assertNotNull(deserializer);
        assertEquals(MyOtherDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typeId = "com.unknown.UnknownClass";
        TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(null, idResolver, "typePropertyName", true, null);
        try {
            JsonDeserializer<Object> deserializer = typeDeserializer._handleUnknownTypeId(new TestContext(), typeId, idResolver, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static class MyTypeDeserializerBase extends TypeDeserializerBase {
        MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        MyTypeDeserializerBase(BeanProperty property, JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
            _property = property;
        }

        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return new MyTypeDeserializerBase(prop, _baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
        }
    }

    private static class MyClassType extends TypeReference<Class<?>> {
        MyClassType(Class<?> clazz) {
            super();
            _value = clazz;
        }
    }

    private static class MyDeserializer extends JsonDeserializer<Object> {
    }

    private static class MyOtherDeserializer extends JsonDeserializer<Object> {
    }

    private static class MyOtherClass {
    }

    private static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new TestObjectMapper(), null, null, null, false, null, null);
        }

        @Override
        public JavaType findTypeFromId(String id, DeserializationFeature feature) throws IOException {
            return null;
        }
    }
}