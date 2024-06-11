import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.JavaType;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        assertNotNull(mapType);
        assertEquals(String.class, mapType.getRawClass());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getContentValueHandler());
        assertFalse(mapType.isStatic());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        JavaType newKeyType = TypeBase.defaultInstance(String.class);
        MapType newMapType = mapType.withKeyType(newKeyType);
        assertNotNull(newMapType);
        assertEquals(String.class, newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
        assertNull(newMapType.getContentValueHandler());
        assertFalse(newMapType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        JavaType newContentType = TypeBase.defaultInstance(Float.class);
        MapType newMapType = mapType.withContentType(newContentType);
        assertNotNull(newMapType);
        assertEquals(String.class, newMapType.getRawClass());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(newContentType, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
        assertNull(newMapType.getContentValueHandler());
        assertFalse(newMapType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertEquals(String.class, newMapType.getRawClass());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
        assertNull(newMapType.getContentValueHandler());
        assertTrue(newMapType.isStatic());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        Object keyValueHandler = new Object();
        MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(newMapType);
        assertEquals(String.class, newMapType.getRawClass());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
        assertEquals(keyValueHandler, newMapType.getContentValueHandler());
        assertFalse(newMapType.isStatic());
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        String expected = "[map type; class java.lang.String, java.lang.Integer -> java.lang.Double]";
        assertEquals(expected, mapType.toString());
    }
}