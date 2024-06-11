import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    // Test cases for the construct method

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
    }

    // Test cases for the withTypeHandler method

    @Test
    public void testWithTypeHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withTypeHandler("typeHandler");
        assertNotNull(newMapType);
        assertEquals("typeHandler", newMapType.getTypeHandler());
    }

    // Test cases for the withContentTypeHandler method

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withContentTypeHandler("contentTypeHandler");
        assertNotNull(newMapType);
        assertEquals("contentTypeHandler", newMapType.getValueType().getTypeHandler());
    }

    // Test cases for the withValueHandler method

    @Test
    public void testWithValueHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withValueHandler("valueHandler");
        assertNotNull(newMapType);
        assertEquals("valueHandler", newMapType.getValueHandler());
    }

    // Test cases for the withContentValueHandler method

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withContentValueHandler("contentValueHandler");
        assertNotNull(newMapType);
        assertEquals("contentValueHandler", newMapType.getValueType().getValueHandler());
    }

    // Test cases for the withStaticTyping method

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertTrue(newMapType.isStaticTyping());
    }

    // Test cases for the withContentType method

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withContentType(Long.class);
        assertNotNull(newMapType);
        assertEquals(Long.class, newMapType.getValueType());
    }

    // Test cases for the withKeyType method

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withKeyType(Integer.class);
        assertNotNull(newMapType);
        assertEquals(Integer.class, newMapType.getKeyType());
    }

    // Test cases for the refine method

    @Test
    public void testRefine() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        JavaType javaType = mapType.refine(TreeMap.class, null, null, null);
        assertNotNull(javaType);
        assertTrue(javaType instanceof MapType);
        assertEquals(TreeMap.class, javaType.getRawClass());
    }

    // Test cases for the withKeyTypeHandler method

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withKeyTypeHandler("keyTypeHandler");
        assertNotNull(newMapType);
        assertEquals("keyTypeHandler", newMapType.getKeyType().getTypeHandler());
    }

    // Test cases for the withKeyValueHandler method

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapType newMapType = mapType.withKeyValueHandler("keyValueHandler");
        assertNotNull(newMapType);
        assertEquals("keyValueHandler", newMapType.getKeyType().getValueHandler());
    }

}

