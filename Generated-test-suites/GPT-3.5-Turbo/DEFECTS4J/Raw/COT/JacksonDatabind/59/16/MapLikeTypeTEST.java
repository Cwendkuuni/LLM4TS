
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapType_NotMapType() {
        MapLikeType mapLikeType = new MapLikeType(List.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType result = mapLikeType.withKeyType(new JavaType(Long.class));
        assertEquals(new JavaType(Long.class), result.getKeyType());
        assertEquals(valueType, result.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType result = mapLikeType.withContentType(new JavaType(Long.class));
        assertEquals(keyType, result.getKeyType());
        assertEquals(new JavaType(Long.class), result.getValueType());
    }

    @Test
    public void testWithStaticTyping_IsStatic() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType result = mapLikeType.withStaticTyping();
        assertTrue(result.isStatic());
    }

    @Test
    public void testWithStaticTyping_IsNotStatic() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, true);
        MapLikeType result = mapLikeType.withStaticTyping();
        assertTrue(result.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType result = mapLikeType.withKeyTypeHandler(new Object());
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType result = mapLikeType.withValueHandler(new Object());
        assertNotNull(result);
    }

    @Test
    public void testEquals_SameInstance() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.equals(mapLikeType));
    }

    @Test
    public void testEquals_Null() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType.equals(new Object()));
    }

    @Test
    public void testEquals_Equal() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testEquals_NotEqual_Class() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(List.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testEquals_NotEqual_KeyType() {
        JavaType keyType1 = new JavaType(String.class);
        JavaType keyType2 = new JavaType(Integer.class);
        JavaType valueType1 = new JavaType(Long.class);
        JavaType valueType2 = new JavaType(Double.class);

        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType1, valueType1, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType2, valueType1, null, null, false);
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }
    
    @Test
    public void testEquals_NotEqual_ValueType() {
        JavaType keyType1 = new JavaType(String.class);
        JavaType keyType2 = new JavaType(Integer.class);
        JavaType valueType1 = new JavaType(Long.class);
        JavaType valueType2 = new JavaType(Double.class);

        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType1, valueType1, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType1, valueType2, null, null, false);
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testToString() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map-like type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }
}
