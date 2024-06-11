package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayType() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    // Test methods for _narrow

    @Test
    public void testNarrowSameClass() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls);
        SimpleType narrowType = baseType._narrow(String.class);
        assertEquals(baseType, narrowType);
    }

    @Test
    public void testNarrowSubclass() {
        Class<?> superclass = String.class;
        Class<?> subclass = StringBuffer.class;
        SimpleType baseType = new SimpleType(superclass);
        SimpleType narrowType = baseType._narrow(subclass);
        assertEquals(subclass, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowDifferentClass() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType baseType = new SimpleType(cls1);
        baseType._narrow(cls2);
    }

    // Test methods for withTypeHandler

    @Test
    public void testWithTypeHandlerSameHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType newType = baseType.withTypeHandler(null);
        assertEquals(baseType, newType);
    }

    @Test
    public void testWithTypeHandlerDifferentHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, null, null, null, null, null, false);
        Object handler = new Object();
        SimpleType newType = baseType.withTypeHandler(handler);
        assertNotSame(baseType, newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    // Test methods for withValueHandler

    @Test
    public void testWithValueHandlerSameHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType newType = baseType.withValueHandler(null);
        assertEquals(baseType, newType);
    }

    @Test
    public void testWithValueHandlerDifferentHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, null, null, null, null, null, false);
        Object handler = new Object();
        SimpleType newType = baseType.withValueHandler(handler);
        assertNotSame(baseType, newType);
        assertEquals(handler, newType.getValueHandler());
    }

    // Test methods for isContainerType

    @Test
    public void testIsContainerTypeFalse() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    // Test methods for getErasedSignature and getGenericSignature

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignatureWithGenerics() {
        Class<?> cls = Map.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType keyType = typeFactory.constructType(String.class);
        JavaType valueType = typeFactory.constructType(Integer.class);
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test methods for equals

    @Test
    public void testEqualsSameObject() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsNull() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.equals(null));
    }

    // Test methods for buildCanonicalName

    @Test
    public void testBuildCanonicalNameForSimpleType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalNameForSimpleTypeWithGenerics() {
        Class<?> cls = Map.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType keyType = typeFactory.constructType(String.class);
        JavaType valueType = typeFactory.constructType(Integer.class);
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    // Edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithNullClass() {
        SimpleType.constructUnsafe(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        SimpleType.construct(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNullClass() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType._narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerWithNullHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerWithNullHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withValueHandler(null);
    }

}