package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBase(new JavaType(), new TypeIdResolverImpl(), "type",
                true, new JavaType()) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseType() {
        assertEquals(new JavaType(), typeDeserializerBase.baseType());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(new TypeIdResolverImpl(), typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(new JavaType().getRawClass(), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.util.TypeEvaluator", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:new JavaType(); id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl;]", typeDeserializerBase.toString());

    }

    @Test
    public void test_findDeserializer() throws IOException {
        // TODO: Add test case for _findDeserializer method
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        // TODO: Add test case for _findDefaultImplDeserializer method
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {
        // TODO: Add test case for _deserializeWithNativeTypeId method
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        // TODO: Add test case for _handleUnknownTypeId method
    }

    @Test
    public void test_handleMissingTypeId() throws IOException {
        // TODO: Add test case for _handleMissingTypeId method
    }
}