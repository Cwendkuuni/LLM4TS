package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(java.util.ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.TypeFlag.COLLECTION_LIKE, collectionLikeType.getFlags());
        assertEquals(JavaType.TypeFlag.NONE, collectionLikeType._elementType.getFlags());
        assertEquals(Integer.TYPE, collectionLikeType._elementType.getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = java.util.ArrayList.class.getGenericType();
        JavaType baseType = JavaType.construct(javaType.getComponentType());
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, Integer.TYPE);
        assertEquals(java.util.ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.TypeFlag.COLLECTION_LIKE, collectionLikeType.getFlags());
        assertEquals(JavaType.TypeFlag.NONE, collectionLikeType._elementType.getFlags());
        assertEquals(Integer.TYPE, collectionLikeType._elementType.getRawClass());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertTrue(javaType.isTrueCollectionType());
        JavaType javaType2 = CollectionLikeType.construct(java.util.LinkedHashSet.class, null, null, null, java.lang.String.class);
        assertFalse(javaType2.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertEquals(java.lang.Integer.TYPE, javaType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertNull(javaType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertNull(javaType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertFalse(javaType.hasHandlers());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        JavaType javaType2 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        JavaType javaType3 = CollectionLikeType.construct(java.util.LinkedHashSet.class, null, null, null, java.lang.String.class);
        assertEquals(javaType1, javaType1);
        assertEquals(javaType1, javaType2);
        assertNotEquals(javaType1, javaType3);
    }

    @Test
    public void testToString() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        String expected = "[collection-like type; class java.util.ArrayList, contains class int]";
        String result = javaType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testRefine() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        JavaType javaTypeRefined = javaType.refine(java.util.List.class, null, null, null);
        assertEquals(java.util.List.class, javaTypeRefined.getRawClass());
        JavaType javaTypeElement = javaTypeRefined._elementType;
        assertEquals(JavaType.TypeFlag.NONE, javaTypeElement.getFlags());
        assertEquals(java.lang.Integer.TYPE, javaTypeElement.getRawClass());
    }
}