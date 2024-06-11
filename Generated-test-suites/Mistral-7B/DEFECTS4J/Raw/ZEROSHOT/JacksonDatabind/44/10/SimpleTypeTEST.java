package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertTrue(simpleType.isContainerType()); // This should return false
        assertEquals(0, simpleType.getContentType().size());
        assertEquals(0, simpleType.getTypeHandlers().length);
        assertEquals(0, simpleType.getValueHandlers().length);
    }

    @Test
    public void testSimpleTypeConstructionWithBindingsAndSuperTypes() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, Arrays.asList(IntegerType.getInstance()));
        assertEquals(cls, simpleType.getRawClass());
        assertTrue(simpleType.isContainerType()); // This should return false
        assertEquals(1, simpleType.getContentType().size());
        JavaType contentType = simpleType.getContentType().get(0);
        assertEquals(Integer.class, contentType.getRawClass());
        assertEquals(0, simpleType.getTypeHandlers().length);
        assertEquals(0, simpleType.getValueHandlers().length);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType originalSimpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(cls, copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(originalSimpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleTypeWithValueHandler = new SimpleType(simpleType, null, null, null, "ValueHandler", null, false);
        assertEquals(cls, simpleTypeWithValueHandler.getRawClass());
        assertNotSame(simpleType.getValueHandler(), simpleTypeWithValueHandler.getValueHandler());
        assertEquals("ValueHandler", simpleTypeWithValueHandler.getValueHandler().getClass().getName());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleTypeWithTypeHandler = new SimpleType(simpleType, null, null, null, null, "TypeHandler", false);
        assertEquals(cls, simpleTypeWithTypeHandler.getRawClass());
        assertNotSame(simpleType.getTypeHandler(), simpleTypeWithTypeHandler.getTypeHandler());
        assertEquals("TypeHandler", simpleTypeWithTypeHandler.getTypeHandler().getClass().getName());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidClass() {
        SimpleType simpleType = new SimpleType(Object[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        SimpleType simpleType = SimpleType.construct(Object[].class);
    }
}