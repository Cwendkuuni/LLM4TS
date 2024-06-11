package com.example.maptype;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testDefaultMapTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals("{class Map, java.lang.String -> java.lang.Integer}", mapType.toString());
    }

    @Test
    public void testMapTypeConstruction() {
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals("{class Map, java.lang.String -> java.lang.Integer}", mapType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeNullArguments() {
        JavaType keyType = null;
        JavaType valueType = null;
        try {
            MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        } catch (IllegalArgumentException e) {
            assertEquals("keyType and valueType cannot be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeInvalidRawType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        try {
            MapType mapType = MapType.construct(Object.class, null, null, null, keyType, valueType);
        } catch (IllegalArgumentException e) {
            assertEquals("Raw type [class java.lang.Object] is not a Map type", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeUnsupportedSuperInterfaces() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType[] superInterfaces = new JavaType[] {TypeFactory.defaultInstance().constructType(List.class)};
        try {
            MapType mapType = MapType.construct(Map.class, null, null, superInterfaces, keyType, valueType);
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported superinterfaces in MapType: [class java.util.List]", e.getMessage());
        }
    }
}