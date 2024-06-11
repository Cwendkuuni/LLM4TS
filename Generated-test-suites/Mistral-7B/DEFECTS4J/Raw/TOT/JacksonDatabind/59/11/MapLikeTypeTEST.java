import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        // Test default constructor
        JavaType mapType = null;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, bindings, superClass,
                superInts, keyT, valueT, null, null, false);
        assertEquals("MapLikeType with default constructor", mapLikeType1,
                new MapLikeType(Map.class, bindings, superClass, superInts,
                        keyT, valueT));

        // Test constructor with arguments
        mapType = TypeBase.construct(Map.class, keyT, valueT);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, keyT, valueT);
        assertEquals("MapLikeType constructed with arguments", mapLikeType1,
                mapLikeType2);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);
        MapLikeType mapLikeType1 = MapLikeType.upgradeFrom(baseType, keyT,
                valueT);
        assertEquals("MapLikeType upgraded from HashMap", mapLikeType1,
                new MapLikeType(Map.class, null, null, null, keyT, valueT));

        // Test invalid argument
        try {
            MapLikeType.upgradeFrom(Integer.class, keyT, valueT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Expected IllegalArgumentException when upgrading from "
                    + "Integer", e.getMessage(), "Can not upgrade from an instance of class java.lang.Integer");
        }
    }

    @Test
    public void testGetKeyTypeAndValueType() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType = new MapLikeType(mapType, keyT, valueT);
        assertEquals("MapLikeType key type", keyT, mapLikeType.getKeyType());
        assertEquals("MapLikeType value type", valueT, mapLikeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        JavaType newKeyT = JavaType.getType(Integer.class, Object.class);
        MapLikeType mapLikeType2 = mapLikeType1.withKeyType(newKeyT);
        assertEquals("MapLikeType with new key type", newKeyT, mapLikeType2.getKeyType());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        JavaType newValueT = JavaType.getType(String.class, Object.class);
        MapLikeType mapLikeType2 = mapLikeType1.withContentType(newValueT);
        assertEquals("MapLikeType with new value type", newValueT, mapLikeType2.getValueType());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        Object handler = new Object();
        MapLikeType mapLikeType2 = mapLikeType1.withTypeHandler(handler);
        assertEquals("MapLikeType with new type handler", handler, mapLikeType2.getTypeHandler());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        Object handler = new Object();
        JavaType newValueT = JavaType.getType(String.class, Object.class);
        MapLikeType mapLikeType2 = mapLikeType1.withContentTypeHandler(handler);
        assertEquals("MapLikeType with new content type handler", newValueT, mapLikeType2.getValueType());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        Object handler = new Object();
        MapLikeType mapLikeType2 = mapLikeType1.withValueHandler(handler);
        assertEquals("MapLikeType with new value handler", handler, mapLikeType2.getValueHandler());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapType, keyT, valueT);
        Object handler = new Object();
        MapLikeType mapLikeType2 = mapLikeType1.withContentValueHandler(handler);
        assertEquals("MapLikeType with new content value handler", handler, mapLikeType2.getValueHandler());
        assertEquals("Original MapLikeType not changed", mapLikeType1, mapLikeType1);
    }

    @Test
    public void testIsContainerTypeAndIsMapLikeType() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType = new MapLikeType(mapType, keyT, valueT);
        assertTrue("MapLikeType is container type", mapLikeType.isContainerType());
        assertTrue("MapLikeType is map-like type", mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = JavaType.getType(HashMap.class, Object.class);
        JavaType keyT = JavaType.getType(String.class, Object.class);
        JavaType valueT = JavaType.getType(Integer.class, Object.class);

        MapLikeType mapLikeType = new MapLikeType(mapType, keyT, valueT);
        assertTrue("MapLikeType is true map type", mapLikeType.isTrueMapType());

        JavaType listType = JavaType.getType(ArrayList.class, Object.class);
        MapLikeType mapLikeListType = new MapLikeType(listType, keyT, valueT);
        assertFalse("List is not a true map type", mapLikeListType.isTrueMapType());
    }
}