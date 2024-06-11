package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

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
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String typeId = "com.example.TestClass";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String typeId = "unknown.Type";
        try {
            typeDeserializer._handleUnknownTypeId(ctxt, typeId, new StubTypeIdResolver(), typeDeserializer._baseType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, null);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    @Test
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String typeId = "invalid.TypeId";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNull(deserializer);
    }

    @Test
    public void testForProperty() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        BeanProperty property = new TestBeanProperty();
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(property);
        assertNotNull(newTypeDeserializer);
        assertEquals(typeDeserializer.getClass(), newTypeDeserializer.getClass());
        assertEquals(property, newTypeDeserializer._property);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        JsonTypeInfo.As typeInclusion = typeDeserializer.getTypeInclusion();
        assertTrue(typeInclusion == JsonTypeInfo.As.EXISTING_PROPERTY);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String baseTypeName = typeDeserializer.baseTypeName();
        assertEquals(TestBaseClass.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws IOException {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String propertyName = typeDeserializer.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase();
        String toString = typeDeserializer.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase; base-type:"));
    }

    private static class TestContext
            extends DeserializationContext {
        public TestContext() {
            super(new TestObjectMapper(), null, null, null, null, null, null);
        }
    }

    private static class TestBeanProperty
            extends BeanProperty {
        public TestBeanProperty() {
            super("testProperty", TestBaseClass.class, null);
        }
    }

    private static class TestTypeDeserializerBase
            extends TypeDeserializerBase {
        public TestTypeDeserializerBase() {
            super(TestBaseClass.getJavaType(TestObjectMapper.getDefaultInstance()),
                    new StubTypeIdResolver(), "testPropertyName", false, TestDefaultClass.class);
        }

        public TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializerBase(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }
    }

    private static class TestBaseClass {
        public static JavaType getJavaType(ObjectMapper mapper) {
            return mapper.getTypeFactory().constructType(TestBaseClass.class);
        }
    }

    private static class StubTypeIdResolver
            implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    private static class TestDefaultClass {
    }
}