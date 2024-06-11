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
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idResolver = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, idResolver, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "testTypeId");
        assertEquals("TestDeserializer", deserializer.getClass().getSimpleName());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, null, null, false, TestDefaultImpl.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(TestDefaultImpl.class, deserializer.getClass());
    }

    @Test
    public void testFindContextualValueDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idResolver = new TestTypeIdResolver();
        TestProperty property = new TestProperty();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, idResolver, property.getName(), false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "testTypeId");
        assertNull(deserializer);

        JavaType testType = ctxt.getTypeFactory().constructType(TestClass.class);
        deserializer = ctxt.findContextualValueDeserializer(testType, property);
        assertEquals(TestDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, null, null, false, null);
        JavaType javaType = ctxt.getTypeFactory().constructType(UnknownType.class);
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, null, null, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No (native) type id found when one was expected for polymorphic type handling", e.getMessage());
        }

        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", null, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected type id 'unknownTypeId' for deserialization", e.getMessage());
        }
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idResolver = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Object.class, idResolver, null, false, null);
        JavaType javaType = ctxt.getTypeFactory().constructType(UnknownType.class);
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, null, idResolver, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id", e.getMessage());
        }

        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", idResolver, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected type id 'unknownTypeId' for deserialization", e.getMessage());
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public JavaType handleUnknownTypeId(JavaType baseType, String typeId, TypeIdResolver idResolver, String description) throws IOException {
            fail("Should not be called");
            return null;
        }
    }

    private static class TestTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String getDescForKnownTypeIds() {
            return "known type ids are statically known";
        }

        @Override
        public JavaType idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAsString(Object value) {
            return "testTypeId";
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if ("testTypeId".equals(typeId)) {
                return TestDeserializer.class.getJavaType();
            }
            return null;
        }
    }

    private static class TestProperty {
        String name;

        public TestProperty() {
            this.name = "testProperty";
        }

        public String getName() {
            return name;
        }
    }

    private static class TestClass {
    }

    private static class TestDefaultImpl {
    }

    private static class UnknownType {
    }

    private static class TestDeserializer extends JsonDeserializer<Object> {
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase<Object> {
        TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String propertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, propertyName, typeIdVisible, defaultImpl);
        }

        TestTypeDeserializerBase(TypeDeserializerBase<Object> src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializerBase(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}