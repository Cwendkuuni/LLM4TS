import static org.junit.Assert.*;
import org.junit.Test;

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
        assertFalse(mapType.isStaticTyping());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = String.class;
        MapType mapType = (MapType) TypeFactory.defaultInstance().constructType(Map.class);
        mapType = mapType.withKeyType(keyType);
        assertEquals(keyType, mapType.getKeyType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = Integer.class;
        MapType mapType = (MapType) TypeFactory.defaultInstance().constructType(Map.class);
        mapType = mapType.withContentType(contentType);
        assertEquals(contentType, mapType.getValueType());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = (MapType) TypeFactory.defaultInstance().constructType(Map.class);
        mapType = mapType.withStaticTyping();
        assertTrue(mapType.isStaticTyping());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new KeyTypeHandler();
        MapType mapType = (MapType) TypeFactory.defaultInstance().constructType(Map.class);
        mapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, mapType.getKeyType().getTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new KeyValueHandler();
        MapType mapType = (MapType) TypeFactory.defaultInstance().constructType(Map.class);
        mapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, mapType.getValueType().getValueHandler());
    }
}