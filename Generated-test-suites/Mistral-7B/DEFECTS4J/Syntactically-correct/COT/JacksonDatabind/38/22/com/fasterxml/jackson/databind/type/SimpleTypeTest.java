package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindings() {
        Class<List<Integer>> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.defaultBindings());
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(listClass, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<List<Integer>> listClass = ArrayList.class;
        SimpleType original = new SimpleType(listClass, TypeBindings.defaultBindings());
        SimpleType copy = new SimpleType(original);
        assertInstanceOf(SimpleType.class, copy);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructDeprecated() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType invalidSubclass = new SimpleType(Object.class);
        simpleType._narrow(invalidSubclass.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withHandler);
        assertEquals(simpleType.getRawClass(), withHandler.getRawClass());
        assertEquals(simpleType.getBindings(), withHandler.getBindings());
        assertEquals(simpleType.getSuperClass(), withHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withHandler.getSuperInterfaces());
        assertEquals(simpleType.isStatic(), withHandler.isStatic());
        assertEquals(handler, withHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertNotSame(simpleType, staticType);
        assertEquals(simpleType.getRawClass(), staticType.getRawClass());
        assertEquals(simpleType.getBindings(), staticType.getBindings());
        assertEquals(simpleType.getSuperClass(), staticType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), staticType.getSuperInterfaces());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "[class java.util.List]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String toString = simpleType.toString();
        String expectedToString = "[simple type, class [Ljava.lang.String;] ]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}