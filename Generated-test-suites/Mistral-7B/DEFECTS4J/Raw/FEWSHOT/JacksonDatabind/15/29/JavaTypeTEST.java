package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsContainerType() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType intArrayType = tf.constructType(int[].class);
        JavaType listType = tf.constructType(java.util.List.class);
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);

        assertFalse(intType.isContainerType());
        assertFalse(intArrayType.isContainerType());
        assertFalse(listType.isContainerType());
        assertFalse(listIntType.isContainerType());
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType intArrayType = tf.constructType(int[].class);
        JavaType listType = tf.constructType(java.util.List.class);
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);

        assertFalse(intType.isCollectionLikeType());
        assertFalse(intArrayType.isCollectionLikeType());
        assertTrue(listType.isCollectionLikeType());
        assertTrue(listIntType.isCollectionLikeType());
        assertFalse(mapType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType intArrayType = tf.constructType(int[].class);
        JavaType listType = tf.constructType(java.util.List.class);
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);

        assertFalse(intType.isMapLikeType());
        assertFalse(intArrayType.isMapLikeType());
        assertFalse(listType.isMapLikeType());
        assertFalse(listIntType.isMapLikeType());
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testHasGenericTypes() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType intArrayType = tf.constructType(int[].class);
        JavaType listType = tf.constructType(java.util.List.class);
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);

        assertFalse(intType.hasGenericTypes());
        assertFalse(intArrayType.hasGenericTypes());
        assertTrue(listType.hasGenericTypes());
        assertTrue(listIntType.hasGenericTypes());
        assertTrue(mapType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        JavaType keyType = mapType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        JavaType valueType = mapType.getContentType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testContainedTypeCount() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        assertEquals(2, mapType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        JavaType keyType = mapType.containedType(0);
        JavaType valueType = mapType.containedType(1);
        assertEquals(String.class, keyType.getRawClass());
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testContainedTypeName() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        String keyTypeName = mapType.containedTypeName(0);
        String valueTypeName = mapType.containedTypeName(1);
        assertEquals("java.lang.String", keyTypeName);
        assertEquals("java.lang.Integer", valueTypeName);
    }

    @Test
    public void testGetParameterSource() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        Class<?> parameterSource = mapType.getParameterSource();
        assertEquals(Map.class, parameterSource);
    }

    @Test
    public void testContainedTypeOrUnknown() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.Map.class, String.class, Integer.class);
        JavaType unknownType = tf.unknownType();

        JavaType keyType = mapType.containedTypeOrUnknown(0);
        JavaType valueType = mapType.containedTypeOrUnknown(1);

        assertEquals(String.class, keyType.getRawClass());
        assertEquals(Integer.class, valueType.getRawClass());

        JavaType unknownKeyType = mapType.containedTypeOrUnknown(3);
        JavaType unknownValueType = mapType.containedTypeOrUnknown(4);

        assertEquals(unknownType, unknownKeyType);
        assertEquals(unknownType, unknownValueType);
    }

    @Test
    public void testGetValueHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType stringType = tf.constructType(String.class);

        assertNull(intType.getValueHandler());
        assertNull(stringType.getValueHandler());

        Object intValueHandler = new Object() {
            @SuppressWarnings("unchecked")
            public int value(Object value) {
                return ((Number) value).intValue();
            }
        };

        JavaType intWrapperType = tf.constructType(Integer.class);
        JavaType intWrapperTypeWithHandler = intWrapperType.withValueHandler(intValueHandler);

        assertEquals(intValueHandler, intWrapperTypeWithHandler.getValueHandler());
    }

    @Test
    public void testTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType stringType = tf.constructType(String.class);

        assertNull(intType.getTypeHandler());
        assertNull(stringType.getTypeHandler());

        Object intTypeHandler = new Object() {
            public String toString(int value) {
                return Integer.toString(value);
            }
        };

        JavaType intTypeWithHandler = intType.withTypeHandler(intTypeHandler);

        assertEquals(intTypeHandler, intTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testNarrowBy() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType intWrapperType = tf.constructType(Integer.class);

        JavaType intWrapperTypeNarrowed = intType.narrowBy(Integer.class);
        JavaType intWrapperTypeWidened = intWrapperType.widenBy(int.class);

        assertEquals(intWrapperType, intWrapperTypeNarrowed);
        assertEquals(intType, intWrapperTypeWidened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByInvalid() {
        TypeFactory tf = new TypeFactory();
        JavaType intType = tf.constructType(int.class);
        JavaType stringType = tf.constructType(String.class);

        intType.narrowBy(String.class);
    }

    @Test
    public void testWidenBy() {
        TypeFactory tf = new TypeFactory();
        JavaType intWrapperType = tf.constructType(Integer.class);
        JavaType intType = tf.constructType(int.class);

        JavaType intTypeWidened = intWrapperType.widenBy(int.class);
        JavaType intWrapperTypeNarrowed = intType.narrowBy(Integer.class);

        assertEquals(intType, intTypeWidened);
        assertEquals(intWrapperType, intWrapperTypeNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByInvalid() {
        TypeFactory tf = new TypeFactory();
        JavaType intWrapperType = tf.constructType(Integer.class);
        JavaType stringType = tf.constructType(String.class);

        intWrapperType.widenBy(String.class);
    }

    @Test
    public void testNarrowContentsBy() {
        TypeFactory tf = new TypeFactory();
        JavaType listType = tf.constructType(java.util.List.class, int.class);
        JavaType listStringType = tf.constructType(java.util.List.class, String.class);

        JavaType listIntNarrowed = listStringType.narrowContentsBy(int.class);
        JavaType listInt = tf.constructType(java.util.List.class, int.class);

        assertEquals(listInt, listIntNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByInvalid() {
        TypeFactory tf = new TypeFactory();
        JavaType listType = tf.constructType(java.util.List.class, int.class);
        JavaType listStringType = tf.constructType(java.util.List.class, String.class);

        listType.narrowContentsBy(String.class);
    }

    @Test
    public void testWidenContentsBy() {
        TypeFactory tf = new TypeFactory();
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType listStringType = tf.constructType(java.util.List.class, String.class);

        JavaType listStringWidened = listIntType.widenContentsBy(String.class);
        JavaType listString = tf.constructType(java.util.List.class, String.class);

        assertEquals(listString, listStringWidened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByInvalid() {
        TypeFactory tf = new TypeFactory();
        JavaType listIntType = tf.constructType(java.util.List.class, int.class);
        JavaType listIntArrayType = tf.constructType(java.util.List.class, int[].class);

        listIntType.widenContentsBy(int[].class);
    }
}