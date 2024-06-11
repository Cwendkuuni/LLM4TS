import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setUp() {
        mapType = MapType.construct(Map.class, new TypeBindings(), null, null, null, null);
    }

    @Test
    public void testConstruct() {
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStaticTyping());
    }

    @Test
    public void testConstruct_WithTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        mapType = MapType.construct(Map.class, bindings, null, null, null, null);
        assertEquals(bindings, mapType.getBindings());
    }

    @Test
    public void testConstruct_WithSuperClass() {
        JavaType superClass = new SimpleType(Object.class);
        mapType = MapType.construct(Map.class, null, superClass, null, null, null);
        assertEquals(superClass, mapType.getSuperClass());
    }

    @Test
    public void testConstruct_WithSuperInterfaces() {
        JavaType[] superInterfaces = {new SimpleType(Serializable.class), new SimpleType(Cloneable.class)};
        mapType = MapType.construct(Map.class, null, null, superInterfaces, null, null);
        assertArrayEquals(superInterfaces, mapType.getInterfaces());
    }

    @Test
    public void testConstruct_WithKeyType() {
        JavaType keyType = new SimpleType(String.class);
        mapType = MapType.construct(Map.class, null, null, null, keyType, null);
        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testConstruct_WithValueType() {
        JavaType valueType = new SimpleType(Integer.class);
        mapType = MapType.construct(Map.class, null, null, null, null, valueType);
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler_WithHandler() {
        Object handler = new Object();
        mapType = mapType.withKeyValueHandler(handler);
        assertEquals(handler, mapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithKeyType_WithKeyType() {
        JavaType keyType = new SimpleType(Long.class);
        mapType = mapType.withKeyType(keyType);
        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testWithContentType_WithContentType() {
        JavaType contentType = new SimpleType(Double.class);
        mapType = mapType.withContentType(contentType);
        assertEquals(contentType, mapType.getValueType());
    }

    @Test
    public void testWithContentTypeHandler_WithHandler() {
        Object handler = new Object();
        mapType = mapType.withContentTypeHandler(handler);
        assertEquals(handler, mapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        assertFalse(mapType.isStaticTyping());
        mapType = mapType.withStaticTyping();
        assertTrue(mapType.isStaticTyping());
    }

    @Test
    public void testWithKeyTypeHandler_WithHandler() {
        Object handler = new Object();
        mapType = mapType.withKeyTypeHandler(handler);
        assertEquals(handler, mapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler_WithHandler() {
        Object handler = new Object();
        mapType = mapType.withContentValueHandler(handler);
        assertEquals(handler, mapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithContentValueTypeHandler_WithHandler() {
        Object handler = new Object();
        mapType = mapType.withContentTypeHandler(handler);
        assertEquals(handler, mapType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithSuperClass() {
        JavaType superClass = new SimpleType(Object.class);
        mapType = mapType.withSuperClass(superClass);
        assertEquals(superClass, mapType.getSuperClass());
    }

    @Test
    public void testWithInterfaces() {
        JavaType[] interfaces = {new SimpleType(Serializable.class), new SimpleType(Cloneable.class)};
        mapType = mapType.withInterfaces(interfaces);
        assertArrayEquals(interfaces, mapType.getInterfaces());
    }

    @Test
    public void testWithHandler() {
        Object handler = new Object();
        mapType = mapType.withHandler(handler);
        assertEquals(handler, mapType.getTypeHandler());
    }

    @Test
    public void testToString() {
        String expected = "[map type; class java.util.Map, null -> null]";
        assertEquals(expected, mapType.toString());
    }

}