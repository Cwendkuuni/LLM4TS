package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertEquals(false, mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object keyTypeHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeTypeWithKeyTypeHandler = mapLikeType.withKeyTypeHandler(keyTypeHandler);

        assertNotEquals(mapLikeType, mapLikeTypeWithKeyTypeHandler);
        assertEquals(keyTypeHandler, mapLikeTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueTypeHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeTypeWithContentTypeHandler = mapLikeType.withContentTypeHandler(valueTypeHandler);

        assertNotEquals(mapLikeType, mapLikeTypeWithContentTypeHandler);
        assertEquals(valueTypeHandler, mapLikeTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeTypeWithValueHandler = mapLikeType.withValueHandler(valueHandler);

        assertNotEquals(mapLikeType, mapLikeTypeWithValueHandler);
        assertEquals(valueHandler, mapLikeTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object contentValueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeTypeWithContentValueHandler = mapLikeType.withContentValueHandler(contentValueHandler);

        assertNotEquals(mapLikeType, mapLikeTypeWithContentValueHandler);
        assertEquals(contentValueHandler, mapLikeTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeTypeWithStaticTyping = mapLikeType.withStaticTyping();

        assertNotEquals(mapLikeType, mapLikeTypeWithStaticTyping);
        assertTrue(mapLikeTypeWithStaticTyping.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        MapLikeType mapLikeTypeWithKeyType = mapLikeType.withKeyType(keyType);

        assertNotEquals(mapLikeType, mapLikeTypeWithKeyType);
        assertEquals(keyType, mapLikeTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        MapLikeType mapLikeTypeWithContentType = mapLikeType.withContentType(valueType);

        assertNotEquals(mapLikeType, mapLikeTypeWithContentType);
        assertEquals(valueType, mapLikeTypeWithContentType.getContentType());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);

        String expected = "Map-like type; class class java.util.Map, [Ljava.lang.String;->Ljava.lang.Integer;]";
        String actual = mapLikeType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType1 = JavaType.constructType(Integer.class);
        JavaType keyType2 = JavaType.constructType(String.class);
        JavaType valueType2 = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType2, valueType2);

        assertFalse(mapLikeType1.equals(mapLikeType2));
        assertTrue(mapLikeType1.equals(mapLikeType1));
    }
}