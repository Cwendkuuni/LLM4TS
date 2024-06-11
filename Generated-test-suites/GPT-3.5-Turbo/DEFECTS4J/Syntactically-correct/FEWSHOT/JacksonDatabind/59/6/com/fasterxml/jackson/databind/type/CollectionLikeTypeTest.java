package com.fasterxml.jackson.databind.type;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elementType);
        
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
        assertFalse(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
        assertNull(collectionLikeType.getContentValueHandler());
        assertNull(collectionLikeType.getContentTypeHandler());
        assertFalse(collectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
        assertFalse(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
        assertNull(collectionLikeType.getContentValueHandler());
        assertNull(collectionLikeType.getContentTypeHandler());
        assertFalse(collectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        JavaType newElementType = new SimpleType(Integer.class);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newElementType);
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(newElementType, newCollectionLikeType.getContentType());
        assertFalse(newCollectionLikeType.isTrueCollectionType());
        assertFalse(newCollectionLikeType.isContainerType());
        assertTrue(newCollectionLikeType.isCollectionLikeType());
        assertNull(newCollectionLikeType.getContentValueHandler());
        assertNull(newCollectionLikeType.getContentTypeHandler());
        assertFalse(newCollectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.Integer>;", newCollectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        Object handler = new Object();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(elementType, newCollectionLikeType.getContentType());
        assertFalse(newCollectionLikeType.isTrueCollectionType());
        assertFalse(newCollectionLikeType.isContainerType());
        assertTrue(newCollectionLikeType.isCollectionLikeType());
        assertNull(newCollectionLikeType.getContentValueHandler());
        assertEquals(handler, newCollectionLikeType.getContentTypeHandler());
        assertTrue(newCollectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", newCollectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        Object handler = new Object();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(elementType, newCollectionLikeType.getContentType());
        assertFalse(newCollectionLikeType.isTrueCollectionType());
        assertFalse(newCollectionLikeType.isContainerType());
        assertTrue(newCollectionLikeType.isCollectionLikeType());
        assertEquals(handler, newCollectionLikeType.getContentValueHandler());
        assertNull(newCollectionLikeType.getContentTypeHandler());
        assertTrue(newCollectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", newCollectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(elementType, newCollectionLikeType.getContentType());
        assertFalse(newCollectionLikeType.isTrueCollectionType());
        assertFalse(newCollectionLikeType.isContainerType());
        assertTrue(newCollectionLikeType.isCollectionLikeType());
        assertNull(newCollectionLikeType.getContentValueHandler());
        assertNull(newCollectionLikeType.getContentTypeHandler());
        assertFalse(newCollectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", newCollectionLikeType.getGenericSignature(new StringBuilder()).toString());
        assertTrue(newCollectionLikeType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        CollectionLikeType newCollectionLikeType = collectionLikeType.refine(Collection.class, new TypeBindings(), superClass, superInterfaces);
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(elementType, newCollectionLikeType.getContentType());
        assertFalse(newCollectionLikeType.isTrueCollectionType());
        assertFalse(newCollectionLikeType.isContainerType());
        assertTrue(newCollectionLikeType.isCollectionLikeType());
        assertNull(newCollectionLikeType.getContentValueHandler());
        assertNull(newCollectionLikeType.getContentTypeHandler());
        assertFalse(newCollectionLikeType.hasHandlers());
        assertEquals("java.util.Collection<java.lang.String>;", newCollectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }

}