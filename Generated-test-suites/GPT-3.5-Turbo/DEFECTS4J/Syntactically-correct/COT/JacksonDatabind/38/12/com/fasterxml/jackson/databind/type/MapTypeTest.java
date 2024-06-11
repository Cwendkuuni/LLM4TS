
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), new SimpleType(Object.class),
                new SimpleType[0], new SimpleType(String.class), new SimpleType(Integer.class));

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getContentType().getRawClass());
        assertEquals(Object.class, mapType.getSuperClass());
        assertEquals(0, mapType.getInterfaces().size());
        assertEquals(null, mapType.getKeyValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertFalse(mapType.isContainerType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null,
                new SimpleType(String.class), new SimpleType(Integer.class),
                null, null, false);

        MapType staticMapType = mapType.withStaticTyping();

        assertNotEquals(mapType, staticMapType);
        assertTrue(staticMapType.isContainerType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null,
                new SimpleType(String.class), new SimpleType(Integer.class),
                null, null, false);

        JavaType newKeyType = new SimpleType(Integer.class);

        MapType newMapType = mapType.withKeyType(newKeyType);

        assertNotEquals(mapType, newMapType);
        assertEquals(Integer.class, newMapType.getKeyType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null,
                new SimpleType(String.class), new SimpleType(Integer.class),
                null, null, false);

        JavaType newContentType = new SimpleType(Double.class);

        MapType newMapType = mapType.withContentType(newContentType);

        assertNotEquals(mapType, newMapType);
        assertEquals(Double.class, newMapType.getContentType().getRawClass());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null,
                new SimpleType(String.class), new SimpleType(Integer.class),
                null, null, false);

        assertEquals("[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }
}
