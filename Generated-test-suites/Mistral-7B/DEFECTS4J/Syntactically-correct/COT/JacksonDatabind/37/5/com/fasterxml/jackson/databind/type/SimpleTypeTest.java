package com.example.junit4;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForArray() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Object.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType narrowType = simpleType1._narrow(cls2);
        assertEquals(simpleType2, narrowType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForSubclassNotRelated() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        simpleType1._narrow(cls2); // no exception thrown here, but should be
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testConstructUnsafeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
    }

    @Test
    public void testEqualsMethod() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Object.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testToStringMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String expected = "[simple type, class " + cls.getName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<?> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType elementType = typeFactory.constructType(int.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { elementType });
        StringBuilder expected = new StringBuilder("java.util.List<");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNullClass() {
        new SimpleType(null);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testRefineMethodReturnsNull() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType refinedType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNull(refinedType);
    }
}