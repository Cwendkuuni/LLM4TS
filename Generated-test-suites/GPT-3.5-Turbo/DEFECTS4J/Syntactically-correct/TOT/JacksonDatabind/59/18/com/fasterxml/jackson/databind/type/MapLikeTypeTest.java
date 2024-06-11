import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testWithTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        mapType = mapType.withTypeHandler(handler);

        assertEquals(handler, mapType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        mapType = mapType.withValueHandler(handler);

        assertEquals(handler, mapType.getValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        mapType = mapType.withKeyTypeHandler(handler);

        assertEquals(handler, mapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        mapType = mapType.withKeyValueHandler(handler);

        assertEquals(handler, mapType.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, null, null, null, false);

        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new JavaType();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, contentType, null, null, false);

        assertEquals(contentType, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = new JavaType(null, null, null, null, valueHandler, null, false);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, contentType, null, null, false);

        assertEquals(valueHandler, mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = new JavaType(null, null, null, null, null, typeHandler, false);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, contentType, null, null, false);

        assertEquals(typeHandler, mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(null, null, null, null, null, null, false);
        JavaType valueType = new JavaType(null, null, null, null, null, null, false);

        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);

        assertFalse(mapType.hasHandlers()); // handlers not set

        mapType = mapType.withTypeHandler(new Object());
        assertTrue(mapType.hasHandlers()); // type handler set

        mapType = mapType.withValueTypeHandler(new Object());
        assertTrue(mapType.hasHandlers()); // both handlers set

        mapType = mapType.withValueTypeHandler(null);
        assertTrue(mapType.hasHandlers()); // type handler still set

        mapType = mapType.withTypeHandler(null);
        assertFalse(mapType.hasHandlers()); // handlers not set anymore
    }

    @Test
    public void testBuildCanonicalName() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        assertEquals("java.util.Map", mapType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        JavaType keyType = new JavaType(null, null, null, null, null, null, false);
        JavaType valueType = new JavaType(null, null, null, null, null, null, false);

        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);

        assertEquals(mapType1, mapType2);
    }
}