import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, new JavaType(String.class), null, null, null, false);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertEquals(Integer.class, mapLikeType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, new JavaType(String.class), new JavaType(Integer.class), null, null, false);
        assertEquals(String.class, mapLikeType.withKeyType(new JavaType(Object.class)).getKeyType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, new JavaType(String.class), new JavaType(Integer.class), null, null, false);
        assertEquals(Integer.class, mapLikeType.withContentType(new JavaType(Boolean.class)).getContentType().getRawClass());
    }

    @Test
    public void testWithContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, new JavaType(Integer.class), null, null, false);
        mapLikeType = mapLikeType.withContentValueHandler("handler");
        assertEquals("handler", mapLikeType.getContentType().getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class,
                null, null, null, null, new JavaType(Integer.class), null, null, false);
        mapLikeType = mapLikeType.withContentTypeHandler("handler");
        assertEquals("handler", mapLikeType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class, null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertFalse(mapLikeType.withStaticTyping().isContainerType());
        assertTrue(mapLikeType.withStaticTyping().isMapLikeType());
    }

    @Test
    public void testRefine() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class, null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertEquals(MyMap.class, mapLikeType.refine(MyMap.class, null, null, null).getRawClass());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(MyMap.class, null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertEquals("[map-like type; class com.fasterxml.jackson.databind.type.MapLikeTypeTest$MyMap, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(MyMap.class, null, null, null, null, new JavaType(Integer.class), null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(MyMap.class, null, null, null, null, new JavaType(Integer.class), null, null, false);
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }

    private static class MyMap<K, V> extends HashMap<K, V> {
    }
}