package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
    }

    @Test
    public void testCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);

        assertEquals(original, copy);
        assertSame(original.getRawClass(), copy.getRawClass());
        assertSame(original.getBindings(), copy.getBindings());
        assertSame(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.asStatic(), copy.asStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withHandler);
        assertSame(simpleType.getRawClass(), withHandler.getRawClass());
        assertSame(simpleType.getBindings(), withHandler.getBindings());
        assertSame(simpleType.getSuperClass(), withHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withHandler.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
        assertTrue(withHandler.asStatic());
        assertSame(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        try {
            simpleType.withContentTypeHandler(handler);
            fail("should not reach this point");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withHandler);
        assertSame(simpleType.getRawClass(), withHandler.getRawClass());
        assertSame(simpleType.getBindings(), withHandler.getBindings());
        assertSame(simpleType.getSuperClass(), withHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withHandler.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
        assertFalse(withHandler.asStatic());
        assertSame(handler, withHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType staticType = simpleType.withStaticTyping();
        assertFalse(simpleType.asStatic());
        assertTrue(staticType.asStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithBaseClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        SimpleType narrow = simpleType.withNarrow(stringClass);
        fail("should not reach this point");
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(stringClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(ArrayList.class);
        fail("should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(HashMap.class);
        fail("should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
        fail("should not reach this point");
    }
}