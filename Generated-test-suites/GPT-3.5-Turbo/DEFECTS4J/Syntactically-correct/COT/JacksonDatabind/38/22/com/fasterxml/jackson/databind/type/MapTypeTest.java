import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.hasHandlers());
        assertFalse(mapType.isContainerType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withContentType(Long.class);
        assertEquals(Long.class, newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyType(Double.class);
        assertEquals(Double.class, newMapType.getKeyType());
    }

    @Test
    public void testRefine() {
        MapType baseType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType refinedType = baseType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedType.getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Object keyTypeHandler = new Object(); // Dummy type handler object
        MapType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Object keyValueHandler = new Object(); // Dummy value handler object
        MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
    }
}