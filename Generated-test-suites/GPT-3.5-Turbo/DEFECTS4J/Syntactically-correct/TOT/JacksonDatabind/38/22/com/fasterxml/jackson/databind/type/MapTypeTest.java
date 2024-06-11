
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setUp() {
        mapType = MapType.construct(com.fasterxml.jackson.annotation.JsonAnySetter.class, null,
                com.fasterxml.jackson.annotation.JsonAnySetter.class, null,
                com.fasterxml.jackson.annotation.JsonAnySetter.class, com.fasterxml.jackson.annotation.JsonAnySetter.class);
    }

    @Test
    public void testConstruct() {
        assertNotNull(mapType);
        assertEquals("com.fasterxml.jackson.annotation.JsonAnySetter", mapType.getRawClass().getName());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapType.getSuperClass());
        assertEquals(0, mapType.getInterfaces().size());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapType.getKeyType());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapType.getContentType());
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapTypeWithKeyTypeHandler);
        assertNotSame(mapType, mapTypeWithKeyTypeHandler);
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyType().getTypeHandler());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithKeyTypeHandler.getKeyType().getContentType());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object valueHandler = new Object();
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(valueHandler);
        assertNotNull(mapTypeWithKeyValueHandler);
        assertNotSame(mapType, mapTypeWithKeyValueHandler);
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithKeyValueHandler.getKeyType());
        assertEquals(valueHandler, mapTypeWithKeyValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertNotNull(mapTypeWithStaticTyping);
        assertNotSame(mapType, mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isStaticTyping());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithStaticTyping.getKeyType().getContentType());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithStaticTyping.getContentType());
    }

    @Test
    public void testWithContentType() {
        com.fasterxml.jackson.annotation.JsonAnySetter contentType = new com.fasterxml.jackson.annotation.JsonAnySetter();
        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertNotNull(mapTypeWithContentType);
        assertNotSame(mapType, mapTypeWithContentType);
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithContentType.getKeyType());
        assertEquals(contentType, mapTypeWithContentType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        com.fasterxml.jackson.annotation.JsonAnySetter keyType = new com.fasterxml.jackson.annotation.JsonAnySetter();
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType);
        assertNotNull(mapTypeWithKeyType);
        assertNotSame(mapType, mapTypeWithKeyType);
        assertEquals(keyType, mapTypeWithKeyType.getKeyType());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, mapTypeWithKeyType.getContentType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = com.fasterxml.jackson.annotation.JsonAnySetter.class;
        com.fasterxml.jackson.annotation.JsonAnySetter superClass = new com.fasterxml.jackson.annotation.JsonAnySetter();
        com.fasterxml.jackson.annotation.JsonAnySetter[] superInterfaces = new com.fasterxml.jackson.annotation.JsonAnySetter[2];
        superInterfaces[0] = new com.fasterxml.jackson.annotation.JsonAnySetter();
        superInterfaces[1] = new com.fasterxml.jackson.annotation.JsonAnySetter();
        MapType refinedMapType = mapType.refine(rawType, null, superClass, superInterfaces);
        assertNotNull(refinedMapType);
        assertNotSame(mapType, refinedMapType);
        assertEquals(rawType, refinedMapType.getRawClass());
        assertEquals(superClass, refinedMapType.getSuperClass());
        assertEquals(2, refinedMapType.getInterfaces().size());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, refinedMapType.getKeyType());
        assertEquals(com.fasterxml.jackson.annotation.JsonAnySetter.class, refinedMapType.getContentType());
    }

    @Test
    public void testWithHandle() {
        Object typeHandler = new Object();
        Object valueHandler = new Object();
        MapType mapTypeWithHandle = mapType.withTypeHandler(typeHandler).withValueHandler(valueHandler);
        assertNotNull(mapTypeWithHandle);
        assertNotSame(mapType, mapTypeWithHandle);
        assertEquals(typeHandler, mapTypeWithHandle.getTypeHandler());
        assertEquals(valueHandler, mapTypeWithHandle.getValueHandler());
    }

    @Test
    public void testToString() {
        String toString = mapType.toString();
        assertEquals("[map type; class com.fasterxml.jackson.annotation.JsonAnySetter, com.fasterxml.jackson.annotation.JsonAnySetter -> com.fasterxml.jackson.annotation.JsonAnySetter]", toString);
    }
}
