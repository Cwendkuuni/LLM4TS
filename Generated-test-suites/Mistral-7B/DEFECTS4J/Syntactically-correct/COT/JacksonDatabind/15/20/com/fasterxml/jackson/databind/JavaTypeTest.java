package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {
    

    @Test
    public void testConstructor() {
        // Test the constructor with valid arguments
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals(String.class, javaType.getRawClass());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        // Test the constructor with null rawClass argument
        try {
            new JavaType(null, 0, null, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the constructor with negative additionalHash argument
        try {
            new JavaType(rawClass, -1, null, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetRawClass() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testHasRawClass() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.hasRawClass(String.class));
        assertFalse(javaType.hasRawClass(Integer.class));
    }

    @Test
    public void testIsAbstract() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertFalse(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        Class<?> rawClass = Enum.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isEnumType());
        assertEquals(Enum.class, javaType.getRawClass());
    }

    @Test
    public void testIsInterface() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isInterface());
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test
    public void testIsPrimitive() {
        Class<?> rawClass = boolean.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isPrimitive());
        assertEquals(boolean.class, javaType.getRawClass());
    }

    @Test
    public void testIsFinal() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, true);
        assertTrue(javaType.isFinal());
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testIsThrowable() {
        Class<?> rawClass = Exception.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isThrowable());
        assertEquals(Exception.class, javaType.getRawClass());
    }

    @Test
    public void testIsArrayType() {
        Class<?> rawClass = int[].class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isArrayType());
        assertEquals(int[].class, javaType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isContainerType());
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test
    public void testIsCollectionLikeType() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isCollectionLikeType());
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test
    public void testIsMapLikeType() {
        Class<?> rawClass = Map.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isMapLikeType());
        assertEquals(Map.class, javaType.getRawClass());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.unknownType();
        assertFalse(javaType.useStaticType());

        Class<?> rawClass = String.class;
        javaType = new JavaType(rawClass, 0, null, null, true);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testNarrowByWithSubclass() {
        Class<?> rawClass = String.class;
        JavaType stringType = new JavaType(rawClass, 0, null, null, false);
        JavaType subclassType = new JavaType(StringBuffer.class, 0, null, null, false);

        JavaType result = stringType.narrowBy(StringBuffer.class);
        assertEquals(StringBuffer.class, result.getRawClass());
        assertEquals(stringType, result);

        result = stringType.narrowBy(Object.class);
        assertEquals(Object.class, result.getRawClass());
        assertNotSame(stringType, result);
    }

    @Test
    public void testNarrowByWithNonSubclass() {
        Class<?> rawClass = String.class;
        JavaType stringType = new JavaType(rawClass, 0, null, null, false);
        JavaType intType = new JavaType(int.class, 0, null, null, false);

        try {
            JavaType result = stringType.narrowBy(int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        result = stringType.narrowBy(Object.class);
        assertEquals(Object.class, result.getRawClass());
        assertNotSame(stringType, result);
    }

    @Test
    public void testWidenByWithSuperclass() {
        Class<?> rawClass = StringBuffer.class;
        JavaType stringBufferType = new JavaType(rawClass, 0, null, null, false);
        JavaType stringType = new JavaType(String.class, 0, null, null, false);

        JavaType result = stringBufferType.widenBy(String.class);
        assertEquals(String.class, result.getRawClass());
        assertEquals(stringBufferType, result);
    }

    @Test
    public void testWidenByWithNonSuperclass() {
        Class<?> rawClass = StringBuffer.class;
        JavaType stringBufferType = new JavaType(rawClass, 0, null, null, false);
        JavaType integerType = new JavaType(int.class, 0, null, null, false);

        try {
            JavaType result = stringBufferType.widenBy(int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        result = stringBufferType.widenBy(Object.class);
        assertEquals(Object.class, result.getRawClass());
        assertNotSame(stringBufferType, result);
    }

    @Test
    public void testNarrowContentsByWithSubclass() {
        Class<?> rawClass = List.class;
        JavaType listType = new JavaType(rawClass, 0, null, null, false);
        JavaType stringListType = new JavaType(List.class, 1, String.class, null, false);

        JavaType result = listType.narrowContentsBy(String.class);
        assertEquals(String.class, result.getRawClass());
        assertEquals(listType, result);

        result = listType.narrowContentsBy(Object.class);
        assertNotSame(listType, result);
    }

    @Test
    public void testNarrowContentsByWithNonSubclass() {
        Class<?> rawClass = List.class;
        JavaType listType = new JavaType(rawClass, 0, null, null, false);
        JavaType integerListType = new JavaType(List.class, 1, int.class, null, false);

        try {
            JavaType result = listType.narrowContentsBy(int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        result = listType.narrowContentsBy(Object.class);
        assertNotSame(listType, result);
    }

    @Test
    public void testWidenContentsByWithSuperclass() {
        Class<?> rawClass = List.class;
        JavaType listType = new JavaType(rawClass, 0, null, null, false);
        JavaType integerListType = new JavaType(List.class, 1, int.class, null, false);
        JavaType objectListType = new JavaType(List.class, 0, Object.class, null, false);

        JavaType result = integerListType.widenContentsBy(Object.class);
        assertEquals(List.class, result.getRawClass());
        assertEquals(objectListType, result);
    }

    @Test
    public void testWidenContentsByWithNonSuperclass() {
        Class<?> rawClass = List.class;
        JavaType listType = new JavaType(rawClass, 0, null, null, false);
        JavaType integerListType = new JavaType(List.class, 1, int.class, null, false);

        try {
            JavaType result = integerListType.widenContentsBy(float.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        result = integerListType.widenContentsBy(Object.class);
        assertNotSame(integerListType, result);
    }

    
}