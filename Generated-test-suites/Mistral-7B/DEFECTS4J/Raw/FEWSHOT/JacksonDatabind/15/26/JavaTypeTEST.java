package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.unknownType();
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.unknownType();
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TypeFactory.unknownType();
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.intType();
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.finalClassType(String.class);
        assertTrue(javaType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.javaType(String[].class);
        assertFalse(javaType.useStaticType());

        JavaType javaTypeStatic = javaType.withStaticTyping();
        assertTrue(javaTypeStatic.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertTrue(javaType.hasGenericTypes());

        JavaType javaTypeSimple = TypeFactory.longType();
        assertFalse(javaTypeSimple.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertEquals(String.class, javaType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        assertEquals(String.class, javaType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertEquals(2, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertEquals(Map.class, javaType.containedType(0).getRawClass());
        assertEquals(String.class, javaType.containedType(1).getRawClass());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType mapType = javaType.containedTypeOrUnknown(0);
        assertEquals(Map.class, mapType.getRawClass());

        JavaType unknownType = javaType.containedTypeOrUnknown(2);
        assertNull(unknownType);
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertEquals(Map.class, javaType.getParameterSource());
    }

    @Test
    public void testNarrowBy() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType stringMapType = javaType.narrowBy(StringMap.class);
        assertTrue(stringMapType.isMapLikeType());
        assertEquals(String.class, stringMapType.getKeyType().getRawClass());
        assertEquals(String.class, stringMapType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByInvalid() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        javaType.narrowBy(List.class);
    }

    @Test
    public void testWidenBy() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType objectMapType = javaType.widenBy(Object.class);
        assertTrue(objectMapType.isMapLikeType());
        assertEquals(Object.class, objectMapType.getKeyType().getRawClass());
        assertEquals(Object.class, objectMapType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByInvalid() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        javaType.widenBy(String.class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        JavaType listStringType = javaType.narrowContentsBy(String.class);
        assertEquals(String.class, listStringType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByInvalid() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        javaType.narrowContentsBy(Integer.class);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        JavaType listObjectType = javaType.widenContentsBy(Object.class);
        assertEquals(Object.class, listObjectType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByInvalid() {
        JavaType javaType = TypeFactory.collectionType(List.class, String.class);
        javaType.widenContentsBy(String[].class);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("java.util.Map<java.lang.String, java.lang.String>", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.util.Map", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.mapType(Map.class, String.class, String.class);
        assertEquals("class java.util.Map<java.lang.String, java.lang.String>", javaType.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType javaType2 = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType javaType3 = TypeFactory.mapType(List.class, String.class);
        JavaType javaType4 = javaType1;

        assertTrue(javaType1.equals(javaType1));
        assertTrue(javaType1.equals(javaType4));
        assertTrue(javaType1.equals(javaType2));

        assertFalse(javaType1.equals(javaType3));
        assertFalse(javaType1.equals(null));
        assertFalse(javaType1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        JavaType javaType1 = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType javaType2 = TypeFactory.mapType(Map.class, String.class, String.class);
        JavaType javaType3 = TypeFactory.mapType(List.class, String.class);

        int hashcode1 = javaType1.hashCode();
        int hashcode2 = javaType2.hashCode();
        int hashcode3 = javaType3.hashCode();

        assertTrue(hashcode1 == hashcode1);
        assertTrue(hashcode1 == hashcode2);
        assertFalse(hashcode1 == hashcode3);
    }
}