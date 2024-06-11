import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeConstructDeprecated() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeInvalidSuperClass() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Object.class, null, null, null, keyType, valueType);
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withTypeHandler(handler);
        assertNotSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentTypeHandler = JavaType.constructCollectionType(List.class, String.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withContentTypeHandler(handler);
        assertNotSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getContentTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withValueHandler(handler);
        assertNotSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentValueHandler = JavaType.constructCollectionType(List.class, String.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withContentValueHandler(handler);
        assertNotSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotSame(mapType, staticMapType);
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentType = JavaType.constructCollectionType(List.class, String.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertNotSame(mapType, mapTypeWithContentType);
        assertEquals(contentType, mapTypeWithContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType1, valueType);
        JavaType keyType2 = JavaType.constructCollectionType(List.class, String.class);
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertNotSame(mapType, mapTypeWithKeyType);
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(HashMap.class, null, null, null);
        assertNotSame(mapType, refinedMapType);
        assertEquals(HashMap.class, refinedMapType.getRawClass());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(handler);
        assertNotSame(mapType, mapTypeWithKeyTypeHandler);
        assertEquals(handler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(handler);
        assertNotSame(mapType, mapTypeWithKeyValueHandler);
        assertEquals(handler, mapTypeWithKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testMapTypeToString() {
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        String expected = "[map type; class " + Map.class.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapType.toString());
    }
}