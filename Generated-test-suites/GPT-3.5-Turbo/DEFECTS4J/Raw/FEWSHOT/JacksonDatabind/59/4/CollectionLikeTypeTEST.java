package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        assertEquals(elementType, type.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType elementType = new SimpleType(String.class);
        elementType.setValueHandler(valueHandler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        assertEquals(valueHandler, type.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType elementType = new SimpleType(String.class);
        elementType.setTypeHandler(typeHandler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        assertEquals(typeHandler, type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType elementType = new SimpleType(String.class);
        elementType.setValueHandler(valueHandler);
        elementType.setTypeHandler(typeHandler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        assertTrue(type.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        StringBuilder sb = new StringBuilder();
        String expected = "Ljava/util/Collection;";
        type.getErasedSignature(sb);
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        StringBuilder sb = new StringBuilder();
        String expected = "Ljava/util/Collection<Ljava/lang/String;>;";
        type.getGenericSignature(sb);
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        String expected = "java.util.Collection<java.lang.String>";
        assertEquals(expected, type.buildCanonicalName());
    }

}