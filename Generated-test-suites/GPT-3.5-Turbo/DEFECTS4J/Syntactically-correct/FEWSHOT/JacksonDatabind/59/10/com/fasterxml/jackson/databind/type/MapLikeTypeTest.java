package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {
    
    // Test the constructor with all parameters
    @Test
    public void testConstructor() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                new JavaType(), new JavaType(), new Object(), new Object(), false);
        
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(new TypeBindings(), mapLikeType.getBindings());
        assertNull(mapLikeType.getSuperClass());
        assertNull(mapLikeType.getSuperInterfaces());
        assertEquals(new JavaType(), mapLikeType.getKeyType());
        assertEquals(new JavaType(), mapLikeType.getValueType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());
        
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
        assertTrue(mapLikeType.isTrueMapType());
        assertTrue(mapLikeType.hasHandlers());
        
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertNotNull(sb.toString());
        
        StringBuilder sb2 = new StringBuilder();
        mapLikeType.getGenericSignature(sb2);
        assertNotNull(sb2.toString());
        
        assertNotNull(mapLikeType.toString());
        assertTrue(mapLikeType.equals(mapLikeType));
        assertFalse(mapLikeType.equals(null));
        
    }
    
    // Test the constructor with TypeBase and JavaType parameters
    @Test
    public void testConstructorWithTypeBase() {
        TypeBase base = new TypeBase();
        MapLikeType mapLikeType = new MapLikeType(base, new JavaType(), new JavaType());
        
        assertEquals(base, mapLikeType);
        assertEquals(new JavaType(), mapLikeType.getKeyType());
        assertEquals(new JavaType(), mapLikeType.getValueType());
        
    }
    
    // Test the method upgradeFrom
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, new JavaType(), new JavaType());
        
        assertNotNull(mapLikeType);
        
    }
    
    // Test the method refine
    @Test
    public void testRefine() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[1];
        JavaType refinedType = mapLikeType.refine(Map.class, new TypeBindings(), superClass, superInterfaces);
        
        assertNotNull(refinedType);
    }
    
    // Test the method withKeyType
    @Test
    public void testWithKeyType() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        JavaType newKeyType = new JavaType();
        MapLikeType updatedType = mapLikeType.withKeyType(newKeyType);
        
        assertNotNull(updatedType);
        assertEquals(newKeyType, updatedType.getKeyType());
    }
    
    // Test the method withContentType
    @Test
    public void testWithContentType() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        JavaType newContentType = new JavaType();
        MapLikeType updatedType = mapLikeType.withContentType(newContentType);
        
        assertNotNull(updatedType);
        assertEquals(newContentType, updatedType.getContentType());
    }
    
    // Test the method withTypeHandler
    @Test
    public void testWithTypeHandler() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        Object newTypeHandler = new Object();
        MapLikeType updatedType = mapLikeType.withTypeHandler(newTypeHandler);
        
        assertNotNull(updatedType);
        assertEquals(newTypeHandler, updatedType.getTypeHandler());
    }
    
    // Test the method withContentTypeHandler
    @Test
    public void testWithContentTypeHandler() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        Object newContentTypeHandler = new Object();
        MapLikeType updatedType = mapLikeType.withContentTypeHandler(newContentTypeHandler);
        
        assertNotNull(updatedType);
        assertEquals(newContentTypeHandler, updatedType.getContentTypeHandler());
    }
    
    // Test the method withValueHandler
    @Test
    public void testWithValueHandler() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        Object newValueHandler = new Object();
        MapLikeType updatedType = mapLikeType.withValueHandler(newValueHandler);
        
        assertNotNull(updatedType);
        assertEquals(newValueHandler, updatedType.getValueHandler());
    }
    
    // Test the method withContentValueHandler
    @Test
    public void testWithContentValueHandler() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        Object newContentValueHandler = new Object();
        MapLikeType updatedType = mapLikeType.withContentValueHandler(newContentValueHandler);
        
        assertNotNull(updatedType);
        assertEquals(newContentValueHandler, updatedType.getContentValueHandler());
    }
    
    // Test the method withStaticTyping
    @Test
    public void testWithStaticTyping() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                keyT, valueT, new Object(), new Object(), false);
        
        MapLikeType updatedType = mapLikeType.withStaticTyping();
        
        assertNotNull(updatedType);
        assertTrue(updatedType.isStatic());
    }
    
    // Test the method isTrueMapType
    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, 
                new JavaType(), new JavaType(), new Object(), new Object(), false);
        
        assertTrue(mapLikeType.isTrueMapType());
    }
    
}