package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(type.isTrueCollectionType());
        
        CollectionLikeType type2 = new CollectionLikeType(Iterable.class, null, null, null, null, null, null, false);
        assertTrue(type2.isTrueCollectionType());
        
        CollectionLikeType type3 = new CollectionLikeType(Object.class, null, null, null, null, null, null, false);
        assertFalse(type3.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = new SimpleType(Object.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        
        assertEquals(elementType, type.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, valueHandler, null, false);
        
        assertEquals(valueHandler, type.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, typeHandler, false);
        
        assertEquals(typeHandler, type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertFalse(type.hasHandlers());
        
        CollectionLikeType type2 = new CollectionLikeType(Collection.class, null, null, null, null, new Object(), null, false);
        assertTrue(type2.hasHandlers());
        
        CollectionLikeType type3 = new CollectionLikeType(Collection.class, null, null, null, null, null, new Object(), false);
        assertTrue(type3.hasHandlers());
    }

    @Test
    public void testToString() {
        JavaType elementType = new SimpleType(Object.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains " + elementType + "]", type.toString());
    }

}