package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType result = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(result);
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String result = mapType.toString();
        assertNotNull(result);
    }
}