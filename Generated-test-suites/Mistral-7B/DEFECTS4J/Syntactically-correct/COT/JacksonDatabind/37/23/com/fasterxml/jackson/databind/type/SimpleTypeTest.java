package com.yourpackage.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new Class[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        SimpleType simpleType = new SimpleType(int.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class, Integer.class), new JavaType[] { JavaType.construct(Comparable.class, Integer.class) });
        assertEquals(Integer.class, simpleType.getRawClass());
        assertArrayEquals(new Object[] { List.class, Integer.class }, simpleType.getSuperInterfaces());
        assertEquals(JavaType.construct(List.class, Integer.class), simpleType.getSuperClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArrayClass() {
        new SimpleType(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMapClass() {
        new SimpleType(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollectionClass() {
        new SimpleType(ArrayList.class);
    }

    @Test
    public void testNarrowMethod() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(Integer[].class);

        SimpleType result = simpleType1.narrow(Integer[].class);
        assertNull(result);

        result = simpleType2.narrow(Integer.class);
        assertEquals(simpleType1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethod() {
        new SimpleType(String.class).withContentType(JavaType.construct(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerMethod() {
        new SimpleType(String.class).withContentTypeHandler(new Object() {});
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType result = simpleType.withTypeHandler(new Object() {});
        assertNotSame(simpleType, result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertNotSame(simpleType.getTypeHandler(), result.getTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType result = simpleType.withValueHandler(new Object() {});
        assertNotSame(simpleType, result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertNotSame(simpleType.getValueHandler(), result.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType result = simpleType.withStaticTyping();
        assertNotSame(simpleType, result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertTrue(result.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullClass() {
        new SimpleType(null);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(Integer[].class);
        SimpleType simpleType3 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new JavaType[] { Integer.class });

        assertEquals("[simple type, class java.lang.Integer]", simpleType1.buildCanonicalName());
        assertEquals("[simple type, class [I]", simpleType2.buildCanonicalName());
        assertEquals("[simple type, class java.util.List, class java.lang.Integer]", simpleType3.buildCanonicalName());
    }

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(List.class);

        assertFalse(simpleType1.isContainerType());
        assertTrue(simpleType2.isContainerType());
    }

    @Test
    public void testToStringMethod() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new JavaType[] { Integer.class });

        assertEquals("[simple type, class java.lang.Integer]", simpleType1.toString());
        assertEquals("[simple type, class java.util.List, class java.lang.Integer]", simpleType2.toString());
    }

    @Test
    public void testEqualsMethod() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        SimpleType simpleType3 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new JavaType[] { Integer.class });

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

}