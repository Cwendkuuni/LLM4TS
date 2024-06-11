package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {
  
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, JavaType);
        assertTrue(collectionType.isTrueCollectionType());
        
        collectionType = CollectionLikeType.upgradeFrom(JavaType, JavaType);
        assertFalse(collectionType.isTrueCollectionType());
    }
    
    @Test
    public void testGetContentType() {
        JavaType elementType = JavaType;
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, JavaType);
        assertEquals(elementType, collectionType.getContentType());
    }
    
    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType elementType = JavaType.withValueHandler(valueHandler);
        
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elementType);
        assertEquals(valueHandler, collectionType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType elementType = JavaType.withTypeHandler(typeHandler);
        
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elementType);
        assertEquals(typeHandler, collectionType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        JavaType elementType = JavaType.withHandlers(valueHandler, typeHandler);
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elementType);
        
        assertTrue(collectionType.hasHandlers());
    }
    
    @Test
    public void testToString() {
        JavaType elementType = JavaType;
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, JavaType);
        
        String expected = "[collection-like type; class java.util.Collection, contains " + elementType + "]";
        
        assertEquals(expected, collectionType.toString());
    }
    
}