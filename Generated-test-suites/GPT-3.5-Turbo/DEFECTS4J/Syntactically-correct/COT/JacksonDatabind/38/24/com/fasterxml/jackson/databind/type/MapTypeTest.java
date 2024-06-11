import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = com.fasterxml.jackson.databind.type.MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }

    @Test
    public void testConstructWithKeyAndValue() {
        Class<?> rawType = com.fasterxml.jackson.databind.type.MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = MapType.withKeyType(keyType);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = MapType.withContentType(contentType);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType mapType = MapType.withContentTypeHandler(h);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType mapType = MapType.withKeyValueHandler(h);
        assertNotNull(mapType);
        assertEquals(com.fasterxml.jackson.databind.type.MapType.class, mapType.getRawClass());
    }

}