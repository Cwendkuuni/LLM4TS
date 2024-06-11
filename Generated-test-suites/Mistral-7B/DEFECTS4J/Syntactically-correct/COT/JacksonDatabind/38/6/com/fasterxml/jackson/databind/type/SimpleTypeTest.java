package com.yourpackage.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertTrue(simpleType instanceof SimpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithMapClass() {
        Class<?> cls = Map.class;
        new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithCollectionClass() {
        Class<?> cls = Collection.class;
        new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithArrayClass() {
        Class<?> cls = int[].class;
        new SimpleType(cls);
    }

    @Test
    public void testProtectedSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType instanceof SimpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertTrue(simpleType1.getClass() == simpleType2.getClass());
        assertTrue(simpleType1.hashCode() == simpleType2.hashCode());
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertTrue(simpleType instanceof SimpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = DeprecatedException.class)
    public void testDeprecatedConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
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
        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        Type type = new TypeToken<List<String>>() {}.getType();
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[simple type, class java.util.List]<java.lang.String>");
        assertEquals(expected.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testNullClassInConstructor() {
        new SimpleType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHashCodeInConstructor() {
        new SimpleType(String.class, null, null, new JavaType[] { new SimpleType(Integer.class) }, -1, null, false);
    }

    @Test
    public void testConstructWithNullValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertNull(simpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, new Object() {});
    }

    @Test
    public void testConstructWithNullTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertNull(simpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, new Object() {});
    }

    @Test
    public void testConstructWithNullSuperClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertNull(simpleType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidSuperClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, new SimpleType(Integer.class), null, null, null, false);
    }

    @Test
    public void testConstructWithEmptySuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, new JavaType[] {}, null, null, false);
        assertTrue(simpleType.getSuperInterfaces().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullSuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.setSuperInterfaces(null);
    }

    @Test
    public void testSetSuperInterfacesWithEmptyList() {
        Class<?> cls = String.class;
        List<JavaType> emptyList = new ArrayList<>();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.setSuperInterfaces(emptyList);
        assertTrue(simpleType.getSuperInterfaces().equals(emptyList));
    }

    @Test
    public void testSetSuperInterfacesWithList() {
        Class<?> cls = String.class;
        List<JavaType> superInterfaces = new ArrayList<>();
        superInterfaces.add(new SimpleType(Integer.class));
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.setSuperInterfaces(superInterfaces);
        List<JavaType> expectedSuperInterfaces = new ArrayList<>();
        expectedSuperInterfaces.add(new SimpleType(Integer.class));
        assertEquals(expectedSuperInterfaces, simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidSuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, new JavaType[] { new SimpleType(List.class) }, null, null, false);
    }

}