package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeCreation() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        assertEquals("java.lang.String", simpleType.getClassName());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getContentTypeHandler());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        JavaType simpleTypeWithBindings = TypeFactory.defaultInstance()
                .construct(String[].class, TypeBindings.mapType(String.class));
        assertEquals("java.lang.String[]", simpleTypeWithBindings.getClassName());
        assertFalse(simpleTypeWithBindings.isContainerType());
        assertNull(simpleTypeWithBindings.getContentType());
        assertNull(simpleTypeWithBindings.getContentTypeHandler());
        assertNull(simpleTypeWithBindings.getValueHandler());
        assertNull(simpleTypeWithBindings.getTypeHandler());
        assertFalse(simpleTypeWithBindings.isStaticTyping());
        assertEquals(String.class, simpleTypeWithBindings.getContainedType(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapType() {
        TypeFactory.defaultInstance().construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollectionType() {
        TypeFactory.defaultInstance().construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArrayType() {
        TypeFactory.defaultInstance().construct(int[].class);
    }
}