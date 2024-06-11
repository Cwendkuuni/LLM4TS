package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        CollectionLikeType mapLikeType = new CollectionLikeType(Map.class, null, null, null, IntegerType.getInstance(), null, null, false);
        assertTrue(mapLikeType.isContainerType());

        JavaType javaType = null;
        CollectionLikeType nullType = new CollectionLikeType(JavaType.class, null, null, null, StringType.getInstance(), null, null, false);
        assertFalse(nullType.isContainerType());
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType mapLikeType = new CollectionLikeType(Map.class, null, null, null, IntegerType.getInstance(), null, null, false);
        assertFalse(mapLikeType.isTrueCollectionType());

        JavaType javaType = null;
        CollectionLikeType nullType = new CollectionLikeType(JavaType.class, null, null, null, StringType.getInstance(), null, null, false);
        assertFalse(nullType.isTrueCollectionType());
        assertFalse(javaType.isTrueCollectionType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        CollectionLikeType mapLikeType = new CollectionLikeType(Map.class, null, null, null, IntegerType.getInstance(), null, null, false);
        assertTrue(mapLikeType.isCollectionLikeType());

        JavaType javaType = null;
        CollectionLikeType nullType = new CollectionLikeType(JavaType.class, null, null, null, StringType.getInstance(), null, null, false);
        assertFalse(nullType.isCollectionLikeType());
        assertFalse(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = StringType.getInstance();
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, javaType, null, null, false);
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        Object valueHandler = new Object();
        CollectionLikeType collectionLikeTypeWithValueHandler = new CollectionLikeType(Collection.class, null, null, null, null, valueHandler, null, false);
        assertFalse(collectionLikeTypeWithValueHandler.hasHandlers());

        Object typeHandler = new Object();
        CollectionLikeType collectionLikeTypeWithTypeHandler = new CollectionLikeType(Collection.class, null, null, null, null, null, typeHandler, false);
        assertTrue(collectionLikeTypeWithTypeHandler.hasHandlers());

        Object valueHandler2 = new Object();
        Object typeHandler2 = new Object();
        CollectionLikeType collectionLikeTypeWithBothHandlers = new CollectionLikeType(Collection.class, null, null, null, null, valueHandler2, typeHandler2, false);
        assertTrue(collectionLikeTypeWithBothHandlers.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, null, null, null, StringType.getInstance(), null, null, false);
        assertEquals("java.util.List<java.lang.String>", collectionLikeType.buildCanonicalName());
    }
}