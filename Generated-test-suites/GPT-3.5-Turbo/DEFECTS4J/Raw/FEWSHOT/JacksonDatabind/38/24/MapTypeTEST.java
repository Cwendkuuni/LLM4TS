package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertTrue(mapType.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(mapType.getValueType() instanceof ResolvedRecursiveType);
    }
    
    @Test
    public void testConstructDeprecated() {
        MapType mapType = MapType.construct(Map.class, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertTrue(mapType.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(mapType.getValueType() instanceof ResolvedRecursiveType);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithHandler = mapType.withTypeHandler(handler);
        
        assertEquals(Map.class, typeWithHandler.getRawClass());
        assertEquals(handler, typeWithHandler.getTypeHandler());
        assertTrue(typeWithHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithHandler.getValueType() instanceof ResolvedRecursiveType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithHandler = mapType.withContentTypeHandler(handler);
        
        assertEquals(Map.class, typeWithHandler.getRawClass());
        assertTrue(typeWithHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithHandler.getValueType() instanceof ResolvedRecursiveType);
        assertEquals(handler, typeWithHandler.getValueType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithHandler = mapType.withValueHandler(handler);
        
        assertEquals(Map.class, typeWithHandler.getRawClass());
        assertEquals(handler, typeWithHandler.getValueHandler());
        assertTrue(typeWithHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithHandler.getValueType() instanceof ResolvedRecursiveType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithHandler = mapType.withContentValueHandler(handler);
        
        assertEquals(Map.class, typeWithHandler.getRawClass());
        assertTrue(typeWithHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithHandler.getValueType() instanceof ResolvedRecursiveType);
        assertEquals(handler, typeWithHandler.getValueType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType staticType = mapType.withStaticTyping();
        
        assertEquals(Map.class, staticType.getRawClass());
        assertTrue(staticType.getKeyType() instanceof SimpleType);
        assertTrue(staticType.getValueType() instanceof SimpleType);
        assertTrue(staticType.getKeyType().isJavaUtilClass());
        assertTrue(staticType.getValueType().isJavaUtilClass());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithContentType = mapType.withContentType(Integer.class);
        
        assertEquals(Map.class, typeWithContentType.getRawClass());
        assertTrue(typeWithContentType.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithContentType.getValueType() instanceof SimpleType);
        assertTrue(typeWithContentType.getValueType().isJavaUtilClass());
        assertEquals(Integer.class, typeWithContentType.getValueType().getRawClass());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithKeyType = mapType.withKeyType(String.class);
        
        assertEquals(Map.class, typeWithKeyType.getRawClass());
        assertTrue(typeWithKeyType.getKeyType() instanceof SimpleType);
        assertTrue(typeWithKeyType.getKeyType().isJavaUtilClass());
        assertEquals(String.class, typeWithKeyType.getKeyType().getRawClass());
        assertTrue(typeWithKeyType.getValueType() instanceof ResolvedRecursiveType);
    }
    
    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType refinedType = (MapType) mapType.refine(Map.class, new TypeBindings(), null, null);
        
        assertEquals(Map.class, refinedType.getRawClass());
        assertTrue(refinedType.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(refinedType.getValueType() instanceof ResolvedRecursiveType);
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithKeyTypeHandler = mapType.withKeyTypeHandler(handler);
        
        assertEquals(Map.class, typeWithKeyTypeHandler.getRawClass());
        assertTrue(typeWithKeyTypeHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithKeyTypeHandler.getKeyType().getTypeHandler() instanceof Object);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();
        
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType typeWithKeyValueHandler = mapType.withKeyValueHandler(handler);
        
        assertEquals(Map.class, typeWithKeyValueHandler.getRawClass());
        assertTrue(typeWithKeyValueHandler.getKeyType() instanceof ResolvedRecursiveType);
        assertTrue(typeWithKeyValueHandler.getKeyType().getValueHandler() instanceof Object);
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals("[map type; class java.util.Map, ResolvedRecursiveType -> ResolvedRecursiveType]", mapType.toString());
    }
}