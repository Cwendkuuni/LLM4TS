package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsConcrete() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);

        assertTrue(intType.isConcrete());
        assertTrue(stringType.isConcrete());
        assertFalse(javaListType.isConcrete());
    }

    @Test
    public void testIsInterface() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);

        assertFalse(intType.isInterface());
        assertFalse(stringType.isInterface());
        assertTrue(listType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType booleanType = TypeFactory.defaultInstance().constructType(boolean.class);

        assertFalse(stringType.isPrimitive());
        assertFalse(intType.isPrimitive());
        assertTrue(booleanType.isPrimitive());
    }

    @Test
    public void testIsEnumType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType StringEnumType = TypeFactory.defaultInstance().constructType(StringEnumType.class);

        assertFalse(intType.isEnumType());
        assertTrue(StringEnumType.isEnumType());
    }

    @Test
    public void testIsArrayType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(intType.isArrayType());
        assertFalse(stringType.isArrayType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        assertTrue(listType.hasGenericTypes());
        assertFalse(stringType.hasGenericTypes());
    }

    @Test
    public void testContainedType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertEquals(Integer.TYPE, listType.containedType(0));
        assertNull(mapType.containedType(0));
        assertEquals(Map.Entry.class, mapType.containedType(1));
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertEquals(Integer.TYPE, listType.containedTypeOrUnknown(0));
        assertNull(mapType.containedTypeOrUnknown(0));
        assertEquals(Map.Entry.class, mapType.containedTypeOrUnknown(1));
    }

    @Test
    public void testUseStaticType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        assertFalse(intType.useStaticType());
        assertFalse(stringType.useStaticType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertFalse(listType.isMapLikeType());
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertTrue(listType.isCollectionLikeType());
        assertTrue(mapType.isCollectionLikeType());
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertTrue(listType.isContainerType());
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testNarrowBy() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);

        assertEquals(intType, intType.narrowBy(int.class));
        assertEquals(integerType, intType.narrowBy(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByInvalid() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        intType.narrowBy(String.class);
    }

    @Test
    public void testWidenBy() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);

        assertEquals(intType, integerType.widenBy(int.class));
        assertEquals(integerType, integerType.widenBy(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByInvalid() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        intType.widenBy(String.class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType listOfInt = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType listOfInteger = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);

        assertEquals(listOfInt, listOfInteger.narrowContentsBy(int[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByInvalid() {
        JavaType listOfInt = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType listOfString = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);

        listOfInt.narrowContentsBy(String[].class);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType listOfInt = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType listOfInteger = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);

        assertEquals(listOfInt, listOfInteger.widenContentsBy(int[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByInvalid() {
        JavaType listOfInt = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType listOfString = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);

        listOfInt.widenContentsBy(String[].class);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertEquals("java.util.List", listType.getErasedSignature());
        assertEquals("java.util.Map", mapType.getErasedSignature());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        assertEquals("java.util.List<int>", listType.getGenericSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getGenericSignature());
    }
}