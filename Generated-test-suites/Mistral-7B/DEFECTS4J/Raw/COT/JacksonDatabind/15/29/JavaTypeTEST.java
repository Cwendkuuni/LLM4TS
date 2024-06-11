package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    // Basic test case for the constructor
    @Test
    public void testJavaTypeConstruction() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Basic test case for the getRawClass() method
    @Test
    public void testGetRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Basic test case for the hasRawClass() method
    @Test
    public void testHasRawClass() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeInteger = TypeFactory.defaultInstance().constructType(int.class);

        assertTrue(javaTypeString.hasRawClass(String.class));
        assertTrue(javaTypeInteger.hasRawClass(int.class));
        assertFalse(javaTypeString.hasRawClass(int.class));
        assertFalse(javaTypeInteger.hasRawClass(String.class));
    }

    // Basic test case for the isAbstract() method
    @Test
    public void testIsAbstract() {
        JavaType javaTypeArray = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeArray.isAbstract());
        assertFalse(javaTypeString.isAbstract());
    }

    // Basic test case for the isConcrete() method
    @Test
    public void testIsConcrete() {
        JavaType javaTypeArray = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeBoolean = TypeFactory.defaultInstance().constructType(boolean.class);

        assertFalse(javaTypeArray.isConcrete());
        assertTrue(javaTypeString.isConcrete());
        assertTrue(javaTypeBoolean.isConcrete());
    }

    // Basic test case for the isThrowable() method
    @Test
    public void testIsThrowable() {
        JavaType javaTypeException = TypeFactory.defaultInstance().constructType(Exception.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeException.isThrowable());
        assertFalse(javaTypeString.isThrowable());
    }

    // Basic test case for the isArrayType() method
    @Test
    public void testIsArrayType() {
        JavaType javaTypeIntArray = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeIntArray.isArrayType());
        assertFalse(javaTypeString.isArrayType());
    }

    // Basic test case for the isEnumType() method
    @Test
    public void testIsEnumType() {
        JavaType javaTypeEnum = TypeFactory.defaultInstance().constructType(Enum.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeEnum.isEnumType());
        assertFalse(javaTypeString.isEnumType());
    }

    // Basic test case for the isInterface() method
    @Test
    public void testIsInterface() {
        JavaType javaTypeInterface = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeInterface.isInterface());
        assertFalse(javaTypeString.isInterface());
    }

    // Basic test case for the isPrimitive() method
    @Test
    public void testIsPrimitive() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeInt.isPrimitive());
        assertFalse(javaTypeString.isPrimitive());
    }

    // Basic test case for the isFinal() method
    @Test
    public void testIsFinal() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringFinal = TypeFactory.defaultInstance().constructType(String.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        Class<String> stringClass = String.class;
        Class<String> stringFinalClass = stringClass.getName().equals("final String") ? stringClass
                : (Class<String>) typeFactory.constructClass(
                        new StringBuilder("final ").append(stringClass.getSimpleName()).toString());

        assertFalse(javaTypeString.isFinal());
        assertTrue(javaTypeStringFinal.isFinal());
    }

    // Basic test case for the isContainerType() method
    @Test
    public void testIsContainerType() {
        JavaType javaTypeArray = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(javaTypeArray.isContainerType());
        assertFalse(javaTypeString.isContainerType());
    }

    // Basic test case for the isCollectionLikeType() method
    @Test
    public void testIsCollectionLikeType() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertFalse(javaTypeString.isCollectionLikeType());
        assertTrue(javaTypeList.isCollectionLikeType());
    }

    // Basic test case for the isMapLikeType() method
    @Test
    public void testIsMapLikeType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(java.util.Map.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);

        assertFalse(javaTypeString.isMapLikeType());
        assertTrue(javaTypeMap.isMapLikeType());
    }

    // Test for the useStaticType() method
    @Test
    public void testUseStaticType() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringStatic = javaTypeString.withStaticTyping();

        assertFalse(javaTypeString.useStaticType());
        assertTrue(javaTypeStringStatic.useStaticType());
    }

    // Test for the narrowBy() method
    @Test
    public void testNarrowBy() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringSubClass = TypeFactory.defaultInstance().constructType(SubString.class);
        JavaType javaTypeObject = TypeFactory.defaultInstance().constructType(Object.class);

        assertEquals(javaTypeString, javaTypeString.narrowBy(javaTypeString.getRawClass()));
        assertEquals(javaTypeStringSubClass, javaTypeString.narrowBy(javaTypeStringSubClass.getRawClass()));
        assertNull(javaTypeString.narrowBy(javaTypeObject.getRawClass()));
    }

    // Test for the widenBy() method
    @Test
    public void testWidenBy() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeObject = TypeFactory.defaultInstance().constructType(Object.class);

        assertEquals(javaTypeObject, javaTypeString.widenBy(javaTypeObject.getRawClass()));
        assertNull(javaTypeString.widenBy(javaTypeString.getRawClass()));
    }

    // Test for the narrowContentsBy() method
    @Test
    public void testNarrowContentsBy() {
        JavaType javaTypeListOfString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType javaTypeListOfInteger = TypeFactory.defaultInstance().constructType(List.class, int.class);

        assertEquals(javaTypeListOfString, javaTypeListOfString.narrowContentsBy(String.class));
        assertEquals(javaTypeListOfInteger, javaTypeListOfString.narrowContentsBy(int.class));
        assertNull(javaTypeListOfString.narrowContentsBy(boolean.class));
    }

    // Test for the widenContentsBy() method
    @Test
    public void testWidenContentsBy() {
        JavaType javaTypeListOfString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType javaTypeListOfObject = TypeFactory.defaultInstance().constructType(List.class, Object.class);

        assertEquals(javaTypeListOfObject, javaTypeListOfString.widenContentsBy(Object.class));
        assertNull(javaTypeListOfString.widenContentsBy(String.class));
    }

    // Test for edge cases and exception handling scenarios

    // Test for null input
    @Test(expected = NullPointerException.class)
    public void testNarrowByWithNull() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        javaTypeString.narrowBy(null);
    }

    // Test for invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithInvalidInput() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        javaTypeString.narrowBy(int.class);
    }

    // Test for incorrect input
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithIncorrectInput() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeObject = TypeFactory.defaultInstance().constructType(Object.class);
        javaTypeString.narrowBy(javaTypeObject.getRawClass());
    }

    // Test for the _assertSubclass method
    @Test(expected = IllegalArgumentException.class)
    public void testAssertSubclassWithInvalidInput() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        Class<?> intClass = int.class;
        javaTypeString._assertSubclass(intClass, int[].class);
    }

    // Test for the _narrow method
    @SuppressWarnings("unchecked")
    @Test
    public void testNarrowWithSubclass() {
        JavaType javaTypeIntArray = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);

        JavaType javaTypeIntArrayNarrow = javaTypeIntArray._narrow(int[].class);
        JavaType javaTypeIntNarrow = javaTypeInt._narrow(int.class);

        assertEquals(javaTypeIntArray, javaTypeIntArrayNarrow);
        assertEquals(javaTypeInt, javaTypeIntNarrow);
    }

    // Test for the _widen method
    @SuppressWarnings("unchecked")
    @Test
    public void testWidenWithSuperclass() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeIntArray = TypeFactory.defaultInstance().constructType(int[].class);

        JavaType javaTypeIntWiden = javaTypeInt._widen(int[].class);
        JavaType javaTypeIntArrayWiden = javaTypeIntArray._widen(int.class);

        assertEquals(javaTypeIntArray, javaTypeIntWiden);
        assertEquals(javaTypeInt, javaTypeIntArrayWiden);
    }

    // Test for the getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeListOfString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        StringBuilder sb = new StringBuilder();
        javaTypeListOfString.getGenericSignature(sb);

        String expected = "java.util.List<T>";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    // Test for the getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        JavaType javaTypeListOfString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        StringBuilder sb = new StringBuilder();
        javaTypeListOfString.getErasedSignature(sb);

        String expected = "java.util.List";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }
}

class SubString extends String {

}

interface TestInterface {

}

class TestClass implements TestInterface {

}