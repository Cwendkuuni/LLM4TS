import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStaticTyping());
    }

    @Test
    public void testConstructWithRawType() {
        MapType mapType = MapType.construct(HashMap.class, null, null, null, String.class, Integer.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStaticTyping());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType withKeyTypeMapType = mapType.withKeyType(Object.class);
        assertEquals(Map.class, withKeyTypeMapType.getRawClass());
        assertNull(withKeyTypeMapType.getBindings());
        assertNull(withKeyTypeMapType.getSuperClass());
        assertNull(withKeyTypeMapType.getSuperInterfaces());
        assertEquals(Object.class, withKeyTypeMapType.getKeyType());
        assertEquals(Integer.class, withKeyTypeMapType.getValueType());
        assertNull(withKeyTypeMapType.getValueHandler());
        assertNull(withKeyTypeMapType.getTypeHandler());
        assertFalse(withKeyTypeMapType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType withContentTypeMapType = mapType.withContentType(Object.class);
        assertEquals(Map.class, withContentTypeMapType.getRawClass());
        assertNull(withContentTypeMapType.getBindings());
        assertNull(withContentTypeMapType.getSuperClass());
        assertNull(withContentTypeMapType.getSuperInterfaces());
        assertEquals(String.class, withContentTypeMapType.getKeyType());
        assertEquals(Object.class, withContentTypeMapType.getValueType());
        assertNull(withContentTypeMapType.getValueHandler());
        assertNull(withContentTypeMapType.getTypeHandler());
        assertFalse(withContentTypeMapType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType refinedMapType = mapType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedMapType.getRawClass());
        assertNull(refinedMapType.getBindings());
        assertNull(refinedMapType.getSuperClass());
        assertNull(refinedMapType.getSuperInterfaces());
        assertEquals(String.class, refinedMapType.getKeyType());
        assertEquals(Integer.class, refinedMapType.getValueType());
        assertNull(refinedMapType.getValueHandler());
        assertNull(refinedMapType.getTypeHandler());
        assertFalse(refinedMapType.isStaticTyping());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        String expected = "[map type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }

}