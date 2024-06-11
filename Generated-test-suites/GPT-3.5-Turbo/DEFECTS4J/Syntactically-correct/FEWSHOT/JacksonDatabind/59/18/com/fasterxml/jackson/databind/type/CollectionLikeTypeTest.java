package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null,
                                                     null, null, false);
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     null, null, false);
        assertEquals(elementType, type.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elementType = new SimpleType(String.class);
        Object valueHandler = new Object();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     valueHandler, null, false);
        assertEquals(valueHandler, type.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elementType = new SimpleType(String.class);
        Object typeHandler = new Object();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     null, typeHandler, false);
        assertEquals(typeHandler, type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType elementType = new SimpleType(String.class);
        Object valueHandler = new Object();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     valueHandler, null, false);
        assertTrue(type.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null,
                                                     null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append(type.getClass().getName());
        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append(type.getClass().getName());
        expected.append("<");
        expected.append(elementType.toCanonical());
        expected.append(">;");
        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType,
                                                     null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append(type.getClass().getName());
        expected.append("<");
        expected.append(elementType.toCanonical());
        expected.append(">");
        assertEquals(expected.toString(), type.buildCanonicalName());
    }
}