import com.fasterxml.jackson.databind.JavaType;
import org.junit.*;

import static org.junit.Assert.*;

public class MapTypeTest {

    // Test method: construct(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInts, JavaType keyT, JavaType valueT)
    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    // Test method: construct(Class<?> rawType, JavaType keyT, JavaType valueT)
    @Test
    public void testConstruct2() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNotNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    // Test method: _narrow(Class<?> subclass)
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType narrowedType = mapType._narrow(subclass);

        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getInterfaces());
        assertNull(narrowedType.getKeyType());
        assertNull(narrowedType.getValueType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    // Test method: withTypeHandler(Object h)
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withTypeHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType(), newType.getKeyType());
        assertEquals(mapType.getValueType(), newType.getValueType());
        assertEquals(mapType.getValueHandler(), null);
        assertEquals(newType.getTypeHandler(), handler);
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withContentTypeHandler(Object h)
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withContentTypeHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType(), newType.getKeyType());
        assertEquals(mapType.getValueType().getTypeHandler(), handler);
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withValueHandler(Object h)
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withValueHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType(), newType.getKeyType());
        assertEquals(mapType.getValueType(), newType.getValueType());
        assertEquals(newType.getValueHandler(), handler);
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withContentValueHandler(Object h)
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withContentValueHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType(), newType.getKeyType());
        assertEquals(mapType.getValueType().getValueHandler(), handler);
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withStaticTyping()
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withStaticTyping();

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), newType.getKeyType());
        assertEquals(mapType.getValueType().withStaticTyping(), newType.getValueType());
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }

    // Test method: withContentType(JavaType contentType)
    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withContentType(contentType);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType(), newType.getKeyType());
        assertEquals(contentType, newType.getValueType());
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withKeyType(JavaType keyType)
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withKeyType(keyType);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(keyType, newType.getKeyType());
        assertEquals(mapType.getValueType(), newType.getValueType());
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(mapType.getKeyType(), refinedType.getKeyType());
        assertEquals(mapType.getValueType(), refinedType.getValueType());
        assertEquals(mapType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(mapType.isStatic(), refinedType.isStatic());
    }

    // Test method: withKeyTypeHandler(Object h)
    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withKeyTypeHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType().getTypeHandler(), handler);
        assertEquals(mapType.getValueType(), newType.getValueType());
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: withKeyValueHandler(Object h)
    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newType = mapType.withKeyValueHandler(handler);

        assertNotNull(newType);
        assertEquals(mapType.getRawClass(), newType.getRawClass());
        assertEquals(mapType.getBindings(), newType.getBindings());
        assertEquals(mapType.getSuperClass(), newType.getSuperClass());
        assertEquals(mapType.getInterfaces(), newType.getInterfaces());
        assertEquals(mapType.getKeyType().getValueHandler(), handler);
        assertEquals(mapType.getValueType(), newType.getValueType());
        assertEquals(mapType.getValueHandler(), newType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(mapType.isStatic(), newType.isStatic());
    }

    // Test method: toString()
    @Test
    public void testToString() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, keyT, valueT);
        String expectedToStringValue = "[map type; class " + rawType.getName() + ", " + keyT + " -> " + valueT + "]";

        assertEquals(expectedToStringValue, mapType.toString());
    }
}