package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeIdResolver idResolver;
    private JavaType baseType;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        idResolver = new TypeIdResolver();
        baseType = new JavaType();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = new JavaType();
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = new BeanProperty();
        TypeDeserializerBase result = typeDeserializer.forProperty(prop);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase result = typeDeserializer;
        TypeDeserializer.As expected = TypeDeserializer.As.EXTERNAL_PROPERTY;
        assertEquals(expected, result.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase result = typeDeserializer;
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
        assertEquals(expected, result.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase result = typeDeserializer;
        String expected = "type";
        assertEquals(expected, result.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase result = typeDeserializer;
        TypeIdResolver expected = idResolver;
        assertEquals(expected, result.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase result = typeDeserializer;
        Class<?> expected = defaultImpl.getRawClass();
        assertEquals(expected, result.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase result = typeDeserializer;
        String expected = "[TypeDeserializerBase; base-type:com.fasterxml.jackson.databind.JavaType; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolver;]";
        assertEquals(expected, result.toString());
    }

}