package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType() {};
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType valueType = new JavaType() {};
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType valueType = new JavaType() {
            @Override
            public Object getValueHandler() {
                return valueHandler;
            }
        };
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType valueType = new JavaType() {
            @Override
            public Object getTypeHandler() {
                return typeHandler;
            }
        };
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType valueType = new JavaType() {
            @Override
            public Object getValueHandler() {
                return valueHandler;
            }
            @Override
            public Object getTypeHandler() {
                return typeHandler;
            }
        };
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertEquals(mapLikeType1, mapLikeType2);
    }

    @Test
    public void testEqualsWhenDifferent() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertNotEquals(mapLikeType1, mapLikeType2);
    }
}