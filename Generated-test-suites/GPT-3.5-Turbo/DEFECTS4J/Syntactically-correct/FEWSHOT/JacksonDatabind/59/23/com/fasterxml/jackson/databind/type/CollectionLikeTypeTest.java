package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        collectionLikeType = new CollectionLikeType(String.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        assertEquals(String.class, collectionLikeType.getContentType());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Integer.class, null, null, false);
        assertEquals(Integer.class, collectionLikeType.getContentType());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, null, null, false);
        assertEquals(Object.class, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, String.class, "valueHandler", null, false);
        assertEquals("valueHandler", collectionLikeType.getContentValueHandler());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Integer.class, "valueHandler", null, false);
        assertEquals("valueHandler", collectionLikeType.getContentValueHandler());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, "valueHandler", null, false);
        assertEquals("valueHandler", collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, String.class, null, "typeHandler", false);
        assertEquals("typeHandler", collectionLikeType.getContentTypeHandler());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Integer.class, null, "typeHandler", false);
        assertEquals("typeHandler", collectionLikeType.getContentTypeHandler());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, null, "typeHandler", false);
        assertEquals("typeHandler", collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, String.class, "valueHandler", "typeHandler", false);
        assertTrue(collectionLikeType.hasHandlers());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Integer.class, "valueHandler", "typeHandler", false);
        assertTrue(collectionLikeType.hasHandlers());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, "valueHandler", "typeHandler", false);
        assertTrue(collectionLikeType.hasHandlers());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        assertEquals(collectionLikeType1, collectionLikeType2);

        collectionLikeType1 = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        collectionLikeType2 = new CollectionLikeType(Collection.class, null, null, null, Integer.class, null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType2));

        collectionLikeType1 = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        collectionLikeType2 = new CollectionLikeType(String.class, null, null, null, String.class, null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, String.class, null, null, false);
        String expectedString = "[collection-like type; class java.util.Collection, contains class java.lang.String]";
        assertEquals(expectedString, collectionLikeType.toString());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Integer.class, null, null, false);
        expectedString = "[collection-like type; class java.util.Collection, contains class java.lang.Integer]";
        assertEquals(expectedString, collectionLikeType.toString());

        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, Object.class, null, null, false);
        expectedString = "[collection-like type; class java.util.Collection, contains class java.lang.Object]";
        assertEquals(expectedString, collectionLikeType.toString());
    }
}