package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        
        assertNotNull(mapType);
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getContentType());
        assertNull(mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertNotNull(mapType.toString());
    }
    
    @Test
    public void testConstruct_withDeprecatedMethod() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        
        assertNotNull(mapType);
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getContentType());
        assertNull(mapType.getRawClass());
        assertNotNull(mapType.getBindings());
        assertNotNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertNotNull(mapType.toString());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        MapType mapType = new MapType(null, null, null);
        JavaType narrowedType = mapType._narrow(subclass);
        
        assertNotNull(narrowedType);
        assertNull(narrowedType.getKeyType());
        assertNull(narrowedType.getValueType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertNull(narrowedType.getContentType());
        assertNull(narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNotNull(narrowedType.toString());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withTypeHandler(h);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertEquals(h, type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withContentTypeHandler(h);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNotNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertEquals(h, type.getContentType().getTypeHandler());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withValueHandler(h);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNull(type.getValueType());
        assertEquals(h, type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withContentValueHandler(h);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNotNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertEquals(h, type.getValueType().getValueHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withStaticTyping();
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withContentType(contentType);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertEquals(contentType, type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNotNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withKeyType(keyType);
        
        assertNotNull(type);
        assertEquals(keyType, type.getKeyType());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = new MapType(null, null, null);
        JavaType type = mapType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(type);
        assertNull(type.getKeyType());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertSame(superInterfaces, type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withKeyTypeHandler(h);
        
        assertNotNull(type);
        assertEquals(h, type.getKeyType().getTypeHandler());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null);
        MapType type = mapType.withKeyValueHandler(h);
        
        assertNotNull(type);
        assertEquals(h, type.getKeyType().getValueHandler());
        assertNull(type.getValueType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertNull(type.getContentType());
        assertNull(type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNotNull(type.toString());
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null);
        String type = mapType.toString();
        
        assertNotNull(type);
        assertNotNull(type.toString());
    }
}