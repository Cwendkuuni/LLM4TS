package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = MapType.class;
        MapType mapType = new MapType();
        JavaType narrowedType = mapType._narrow(subclass);
        assertNotNull(narrowedType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withTypeHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withContentTypeHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withValueHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withContentValueHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType();
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = new MapType();
        MapType newMapType = mapType.withContentType(contentType);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType();
        MapType newMapType = mapType.withKeyType(keyType);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = new MapType();
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withKeyTypeHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType newMapType = mapType.withKeyValueHandler(h);
        assertNotNull(newMapType);
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType();
        String str = mapType.toString();
        assertNotNull(str);
    }
}