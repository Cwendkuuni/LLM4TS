package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    // Basic test cases for each method

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(),
                null, null, JavaType.constructRawListType(Integer.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertSame(Collection.class, collectionLikeType.getRawClass());
        assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, JavaType.constructRawListType(Integer.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertSame(Collection.class, collectionLikeType.getRawClass());
        assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaType.constructRawListType(Integer.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
        assertInstanceOf(CollectionLikeType.class, collectionLikeType);
        assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        JavaType javaType = JavaType.constructRawListType(Integer.class);
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType javaType = JavaType.constructRawListType(Integer.class);
        assertFalse(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(Object.class, collectionLikeType.getContentType().getRawClass());

        JavaType javaType = JavaType.constructRawListType(String.class);
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertNull(collectionLikeType.getContentValueHandler());

        JavaType javaType = JavaType.constructRawListType(String.class);
        assertEquals(String.class, javaType.getContentValueHandler().getClass());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertNull(collectionLikeType.getContentTypeHandler());

        JavaType javaType = JavaType.constructRawListType(String.class);
        assertNotNull(javaType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        JavaType javaType = JavaType.constructRawListType(String.class);
        assertTrue(javaType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertEquals("java.util.Collection", collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder("[");
        expected.append(Collection.class.getName());
        expected.append("<T>");
        expected.append("]");
        assertEquals(expected.toString(), collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertEquals("java.util.Collection", collectionLikeType.buildCanonicalName());
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidBaseType() {
        CollectionLikeType.construct(String[].class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidElementType() {
        CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        CollectionLikeType.upgradeFrom(new TypeBase() {}, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidElementType() {
        CollectionLikeType.upgradeFrom(JavaType.constructRawListType(Integer[].class), Integer.class);
    }

    @Test
    public void testIsTrueCollectionTypeFalse() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeTrue() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }
}