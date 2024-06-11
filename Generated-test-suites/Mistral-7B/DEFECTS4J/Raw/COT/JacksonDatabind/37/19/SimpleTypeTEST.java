package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Testing constructor 1
    @Test
    public void testSimpleTypeConstructor1() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing constructor 2
    @Test
    public void testSimpleTypeConstructor2() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing constructor 3
    @Test
    public void testSimpleTypeConstructor3() {
        TypeBase base = new TypeBase(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing constructor 4
    @Test
    public void testSimpleTypeConstructor4() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing constructor 5
    @Test
    public void testSimpleTypeConstructor5() {
        SimpleType base = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing constructor 6
    @Test
    public void testSimpleTypeConstructor6() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Testing method: _narrow
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        Class<?> subclass = Integer.class;
        SimpleType simpleType2 = simpleType1._narrow(subclass);
        assertNotNull(simpleType2);
        assertEquals(subclass, simpleType2.getRawClass());
    }

    // Testing method: isContainerType
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Testing methods: getErasedSignature, getGenericSignature
    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        StringBuilder expectedGenericSignature = new StringBuilder();
        expectedGenericSignature.append("<T>");
        StringBuilder expectedErasedSignature = new StringBuilder();
        expectedErasedSignature.append("Ljava/lang/String;");

        SimpleType simpleType = new SimpleType(String[].class);

        StringBuilder actualGenericSignature = simpleType.getGenericSignature(new StringBuilder());
        StringBuilder actualErasedSignature = simpleType.getErasedSignature(new StringBuilder());

        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    // Testing method: toString
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String expected = "[simple type, class " + SimpleType.class.getName() + "]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    // Testing methods: equals
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(String[].class);
        assertFalse(simpleType1.equals(simpleType4));
    }

    // Testing edge cases: constructor with null class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullClass() {
        new SimpleType(null);
    }

    // Testing edge cases: constructor with Array class
    @Test
    public void testConstructorWithArrayClass() {
        SimpleType simpleType = new SimpleType(int[].class);
        JavaType superClass = simpleType.getSuperClass();
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertEquals(Object[].class, superClass.getRawClass());
        assertArrayEquals(new JavaType[0].getClass().getComponentType(), superInterfaces);
    }

    // Testing edge cases: constructor with Collection class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithCollectionClass() {
        new SimpleType(ArrayList.class);
    }

    // Testing edge cases: constructor with Map class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMapClass() {
        new SimpleType(HashMap.class);
    }

    // Testing edge cases: constructor with generic class
    @Test
    public void testConstructorWithGenericClass() {
        Class<?> cls = HashMap.class;
        TypeVariable<Map.Entry<?, ?>> entryTypeVariable = cls.getTypeParameters()[0];
        TypeVariable<Map<?, ?>> mapTypeVariable = cls.getTypeParameters()[1];

        SimpleType simpleType = new SimpleType(cls);
        JavaType superClass = simpleType.getSuperClass();
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertEquals(Map.class, superClass.getRawClass());
        assertArrayEquals(new Class<?>[] { Map.Entry.class, Map.class }, Arrays.stream(superInterfaces).map(JavaType::getRawClass).toArray());

        JavaType mapType = simpleType.getContainedType(0);
        JavaType entryType = simpleType.getContainedType(1);
        JavaType mapTypeArgument = mapType.getGenericTypeArguments()[0];
        JavaType entryTypeArgument = entryType.getGenericTypeArguments()[0];

        assertEquals(Map.Entry.class, entryType);
        assertEquals(Map.class, mapType);
        assertEquals(Map.class, mapTypeArgument.getRawClass());
        assertEquals(Object.class, mapTypeArgument.getGenericTypeArguments()[0].getRawClass());
        assertEquals(Object.class, mapTypeArgument.getGenericTypeArguments()[1].getRawClass());
        assertEquals(JavaType.construct(Map.Entry.class, Map.class, Object.class, Object.class), entryTypeArgument);
    }

    // Testing edge cases: constructor with interface class
    @Test
    public void testConstructorWithInterfaceClass() {
        Class<?> cls = Runnable.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType superClass = simpleType.getSuperClass();
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertEquals(Object.class, superClass.getRawClass());
        assertArrayEquals(new JavaType[] { Runnable.class }, superInterfaces);
    }

    // Testing exception handling: construct with non-class object
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonClassObject() {
        new SimpleType(new Object());
    }

    // Testing exception handling: constructor with primitive data type
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithPrimitiveDataType() {
        new SimpleType(int.class);
    }
}