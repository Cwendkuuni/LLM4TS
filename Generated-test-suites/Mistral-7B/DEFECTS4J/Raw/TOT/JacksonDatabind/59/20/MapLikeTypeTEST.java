import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(new ClassType(HashMap.class), keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testMapLikeTypeGetters() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);

        assertEquals(Map.class, mapLikeType.getClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = new ClassType(String[].class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(new ClassType(Map.class, Integer.class, String.class));

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new ClassType(Map.class, Integer.class, String.class), newMapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Float.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(new ClassType(Number.class, Double.class));

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new ClassType(Number.class, Double.class), newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType keyType = new ClassType(Map.class, Integer.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStaticTyping());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            newMapLikeType = newMapLikeType.withStaticTyping();
        });
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType keyType = new ClassType(Map.class, String.class, String.class);
        JavaType valueType = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(new ClassType(HashMap.class), keyType, valueType);

        String result = mapLikeType.toString();
        String expected = "[map-like type; class class java.util.HashMap, java.lang.String, java.lang.String -> java.lang.Number, java.lang.Integer]";
        assertEquals(expected, result);
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType keyType1 = new ClassType(Map.class, String.class, String.class);
        JavaType valueType1 = new ClassType(Number.class, Integer.class);
        JavaType keyType2 = new ClassType(Map.class, String.class, String.class);
        JavaType valueType2 = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(new ClassType(HashMap.class), keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(new ClassType(HashMap.class), keyType2, valueType2);
        MapLikeType mapLikeType3 = new MapLikeType(new ClassType(HashMap.class), keyType1, valueType1);

        assertTrue(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType keyType1 = new ClassType(Map.class, String.class, String.class);
        JavaType valueType1 = new ClassType(Number.class, Integer.class);
        JavaType keyType2 = new ClassType(Map.class, String.class, String.class);
        JavaType valueType2 = new ClassType(Number.class, Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(new ClassType(HashMap.class), keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(new ClassType(HashMap.class), keyType2, valueType2);

        int hashCode1 = mapLikeType1.hashCode();
        int hashCode2 = mapLikeType2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}