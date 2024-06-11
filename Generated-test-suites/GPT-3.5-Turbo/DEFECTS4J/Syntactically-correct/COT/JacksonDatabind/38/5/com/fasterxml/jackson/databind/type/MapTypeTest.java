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
        assertNull(mapType.getInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyType(Long.class);
        assertNotNull(newMapType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertNull(newMapType.getBindings());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getInterfaces());
        assertEquals(Long.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withContentType(Double.class);
        assertNotNull(newMapType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertNull(newMapType.getBindings());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertNull(newMapType.getBindings());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertTrue(newMapType.isStatic());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyValueHandler(new MyKeyValueHandler());
        assertNotNull(newMapType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertNull(newMapType.getBindings());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNotNull(newMapType.getKeyValueHandler());
        assertFalse(newMapType.isStatic());
    }

    // Test cases for potential edge cases and exceptions can be added here

}