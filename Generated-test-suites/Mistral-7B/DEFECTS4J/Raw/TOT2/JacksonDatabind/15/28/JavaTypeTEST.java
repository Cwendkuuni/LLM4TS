
package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {
    // Test cases for JavaType class

    @Test
    public void testJavaTypeConstruction() {
        Class<?> rawClass = String.class;
        int additionalHash = 7;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, additionalHash, valueHandler, typeHandler, asStatic);

        assertEquals("Expected _class to be the same as passed in", rawClass, javaType._class);
        assertEquals("Expected _hash to be the same as passed in", additionalHash + rawClass.getName().hashCode(), javaType._hash);
        assertNull("Expected _valueHandler to be null if not provided", javaType._valueHandler);
        assertNull("Expected _typeHandler to be null if not provided", javaType._typeHandler);
        assertFalse("Expected _asStatic to be false if not provided", javaType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new JavaType(String[].class, 0, null, null, false);
        Object handler = new Object();
        JavaType expectedJavaType = new JavaType[0].getClass().getComponentType().newInstance().withTypeHandler(handler);

        JavaType actualJavaType = javaType.withTypeHandler(handler);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedJavaType, actualJavaType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new JavaType(Map.class, 0, null, null, false);
        Object handler = new Object();
        JavaType expectedJavaType = new JavaType(HashMap.class).withContentTypeHandler(handler);

        JavaType actualJavaType = javaType.withContentTypeHandler(handler);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedJavaType, actualJavaType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new JavaType(String.class, 0, null, null, false);
        Object handler = new Object();
        JavaType expectedJavaType = new JavaType(String.class).withValueHandler(handler);

        JavaType actualJavaType = javaType.withValueHandler(handler);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedJavaType, actualJavaType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new JavaType(Map.class, 0, null, null, false);
        Object handler = new Object();
        JavaType expectedJavaType = new JavaType(Map.class).withContentValueHandler(handler);

        JavaType actualJavaType = javaType.withContentValueHandler(handler);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedJavaType, actualJavaType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaType(String[].class, 0, null, null, false);
        JavaType staticJavaType = javaType.withStaticTyping();

        assertTrue("Expected useStaticType() to return a JavaType with asStatic set to true", staticJavaType.useStaticType());
    }

    @Test
    public void testNarrow() {
        JavaType stringType = new JavaType(String.class, 0, null, null, false);
        JavaType stringArrayType = new JavaType(String[].class, 0, null, null, false);
        JavaType intType = new JavaType(int.class, 0, null, null, false);

        JavaType narrowedType = stringArrayType.narrowBy(String[].class);
        JavaType forcedNarrowedType = stringArrayType.forcedNarrowBy(String[].class);

        assertEquals("Expected the returned JavaType to be the same as the original one", stringArrayType, narrowedType);
        assertEquals("Expected the returned JavaType to be the same as the original one", stringArrayType, forcedNarrowedType);

        JavaType narrowedIntType = intType.narrowBy(int[].class);
        JavaType forcedNarrowedIntType = intType.forcedNarrowBy(int[].class);

        assertTrue("Expected the returned JavaType to be assignable to the provided type", int[].class.isAssignableFrom(narrowedIntType.getRawClass()));
        assertTrue("Expected the returned JavaType to be assignable to the provided type", int[].class.isAssignableFrom(forcedNarrowedIntType.getRawClass()));
    }

    @Test
    public void testWiden() {
        JavaType stringType = new JavaType(String.class, 0, null, null, false);
        JavaType objectType = new JavaType(Object.class, 0, null, null, false);

        JavaType widenedType = stringType.widenBy(Object.class);
        JavaType forcedWidenedType = stringType.forcedNarrowBy(Object.class);

        assertTrue("Expected the returned JavaType to be assignable to the provided type", Object.class.isAssignableFrom(widenedType.getRawClass()));
        assertTrue("Expected the returned JavaType to be assignable to the provided type", Object.class.isAssignableFrom(forcedWidenedType.getRawClass()));
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType listType = new JavaType(List.class, 0, null, null, false);
        JavaType intType = new JavaType(int.class, 0, null, null, false);

        JavaType narrowedContentsType = listType.narrowContentsBy(intType);
        JavaType expectedNarrowedContentsType = new JavaType(ArrayList.class).withContentType(intType);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedNarrowedContentsType, narrowedContentsType);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType mapType = new JavaType(Map.class, 0, null, null, false);
        JavaType stringType = new JavaType(String.class, 0, null, null, false);

        JavaType widenedContentsType = mapType.widenContentsBy(String.class);
        JavaType expectedWidenedContentsType = new JavaType(java.util.Map.class).withKeyType(String.class);

        assertEquals("Expected the returned JavaType to be the same as the expected one", expectedWidenedContentsType, widenedContentsType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeConstructionWithNullRawClass() {
        new JavaType(null, 0, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeConstructionWithIncompatibleClass() {
        JavaType javaStringType = new JavaType(String.class, 0, null, null, false);
        JavaType javaIntType = new JavaType(int.class, 0, null, null, false);

        JavaType incompatibleType = javaStringType.narrowBy(int.class);

        assertNotSame("Expected the returned JavaType to be different from the original one", javaStringType, incompatibleType);
    }

    @Test(expected = NullPointerException.class)
    public void testJavaTypeWithNullHandler() {
        JavaType javaType = new JavaType(String.class, 0, null, null, false);

        javaType.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWithIncompatibleHandler() {
        JavaType javaStringType = new JavaType(String.class, 0, null, null, false);
        JavaType javaIntegerType = new JavaType(Integer.class, 0, null, null, false);

        JavaType incompatibleType = javaStringType.withTypeHandler(javaIntegerType);

        assertNotSame("Expected the returned JavaType to be different from the original one", javaStringType, incompatibleType);
    }

    // Test cases for contained types and methods
}
