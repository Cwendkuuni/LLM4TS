package org.apache.commons.cli.test; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(javaListType, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());

        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Double.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
        assertEquals(false, newMapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());

        JavaType newValueType = TypeFactory.defaultInstance().constructType(Float.class);
        MapLikeType newMapLikeType = mapLikeType.withValueType(newValueType);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(false, newMapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeWithHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new KeyHandler();
        Object valueHandler = new ValueHandler();

        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, keyHandler, valueHandler, false);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());
        assertEquals(keyHandler, mapLikeType.getKeyValueHandler());
        assertEquals(valueHandler, mapLikeType.getValueHandler());

        Object newKeyHandler = new NewKeyHandler();
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(newKeyHandler);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
        assertEquals(false, newMapLikeType.isStatic());
        assertEquals(newKeyHandler, newMapLikeType.getKeyValueHandler());
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStatic());

        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();

        assertEquals(Map.class, staticMapLikeType
                .getRawClass());
        assertEquals(keyType, staticMapLikeType.getKeyType());
        assertEquals(valueType, staticMapLikeType.getValueType());
        assertEquals(true, staticMapLikeType.isStatic());
    }

    // Add more test cases as needed

    private static JavaType.TypeFactory TypeFactory = new JavaType.TypeFactory() {
        @Override
        public JavaType constructType(Class<?> rawType) {
            return super.constructType(rawType);
        }
    };
}