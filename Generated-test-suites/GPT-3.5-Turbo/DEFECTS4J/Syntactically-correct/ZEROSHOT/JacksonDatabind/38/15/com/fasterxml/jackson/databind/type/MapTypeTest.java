package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MapTypeTest {
    
    private MapType mapType;
    
    @Before
    public void setUp() {
        mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
    }
    
    @Test
    public void testConstruct() {
        MapType constructedMapType = MapType.construct(MapType.class, null, null, null, null, null);
        assertNotNull(constructedMapType);
    }
    
    @Test
    public void testConstructDeprecated() {
        MapType constructedMapType = MapType.construct(MapType.class, null, null);
        assertNotNull(constructedMapType);
    }
    
    @Test
    public void test_narrow() {
        JavaType narrowedType = mapType._narrow(MapType.class);
        assertNotNull(narrowedType);
    }
    
    @Test
    public void testWithTypeHandler() {
        MapType typeWithHandler = mapType.withTypeHandler(null);
        assertNotNull(typeWithHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        MapType typeWithContentTypeHandler = mapType.withContentTypeHandler(null);
        assertNotNull(typeWithContentTypeHandler);
    }
    
    @Test
    public void testWithValueHandler() {
        MapType typeWithValueHandler = mapType.withValueHandler(null);
        assertNotNull(typeWithValueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        MapType typeWithContentValueHandler = mapType.withContentValueHandler(null);
        assertNotNull(typeWithContentValueHandler);
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType typeWithStaticTyping = mapType.withStaticTyping();
        assertNotNull(typeWithStaticTyping);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null; // replace with actual content type
        MapType typeWithContentType = mapType.withContentType(contentType);
        assertNotNull(typeWithContentType);
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null; // replace with actual key type
        MapType typeWithKeyType = mapType.withKeyType(keyType);
        assertNotNull(typeWithKeyType);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null; // replace with actual raw type
        TypeBindings bindings = null; // replace with actual bindings
        JavaType superClass = null; // replace with actual superclass
        JavaType[] superInterfaces = null; // replace with actual super interfaces
        MapType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType typeWithKeyTypeHandler = mapType.withKeyTypeHandler(null);
        assertNotNull(typeWithKeyTypeHandler);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType typeWithKeyValueHandler = mapType.withKeyValueHandler(null);
        assertNotNull(typeWithKeyValueHandler);
    }
    
    @Test
    public void testToString() {
        String toString = mapType.toString();
        assertNotNull(toString);
    }
}