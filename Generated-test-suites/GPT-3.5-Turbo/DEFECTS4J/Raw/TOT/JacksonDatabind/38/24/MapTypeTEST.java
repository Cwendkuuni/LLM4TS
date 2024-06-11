import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstructWithAllParameters() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testDeprecatedConstructWithTwoInlineParameters() {
        Class<?> rawType = MapType.class;
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(rawType, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
        assertNull(mapType.getBindings());
        assertNotNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrowWithNewSubclass() {
        Class<?> subclass = MapType.class;

        MapType mapType = new MapType(TestSubclass.class);

        MapType narrowedMapType = mapType._narrow(subclass);

        assertNotNull(narrowedMapType);
        assertEquals(subclass, narrowedMapType.getRawType());
        assertEquals(mapType.getBindings(), narrowedMapType.getBindings());
        assertEquals(mapType.getSuperClass(), narrowedMapType.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), narrowedMapType.getInterfaces());
        assertEquals(mapType.getKeyType(), narrowedMapType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedMapType.getValueType());
        assertEquals(mapType.getValueHandler(), narrowedMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), narrowedMapType.getTypeHandler());
        assertEquals(mapType.isStatic(), narrowedMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        String handler = "contentHandler";

        MapType mapType = new MapType(TestClass.class);

        MapType withContentTypeHandler = mapType.withContentTypeHandler(handler);

        assertNotNull(withContentTypeHandler);
        assertEquals(mapType.getRawType(), withContentTypeHandler.getRawType());
        assertEquals(mapType.getBindings(), withContentTypeHandler.getBindings());
        assertEquals(mapType.getSuperClass(), withContentTypeHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), withContentTypeHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), withContentTypeHandler.getKeyType());
        assertNotNull(withContentTypeHandler.getValueType());
        assertEquals(handler, withContentTypeHandler.getValueType().getTypeHandler());
        assertEquals(mapType.getValueHandler(), withContentTypeHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), withContentTypeHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        String handler = "contentHandler";

        MapType mapType = new MapType(TestClass.class);

        MapType withContentValueHandler = mapType.withContentValueHandler(handler);

        assertNotNull(withContentValueHandler);
        assertEquals(mapType.getRawType(), withContentValueHandler.getRawType());
        assertEquals(mapType.getBindings(), withContentValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), withContentValueHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), withContentValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), withContentValueHandler.getKeyType());
        assertNotNull(withContentValueHandler.getValueType());
        assertEquals(handler, withContentValueHandler.getValueType().getValueHandler());
        assertEquals(mapType.getValueHandler(), withContentValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), withContentValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTypingWhenAlreadyStatic() {
        MapType mapType = new MapType(TestClass.class).withStaticTyping();

        MapType withStaticTyping = mapType.withStaticTyping();

        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithStaticTypingWhenNotStatic() {
        MapType mapType = new MapType(TestClass.class);

        MapType withStaticTyping = mapType.withStaticTyping();

        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentTypeWhenSameType() {
        MapType mapType = new MapType(TestClass.class);
        JavaType contentType = mapType.getValueType();

        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithContentTypeWhenDifferentType() {
        MapType mapType = new MapType(TestClass.class);
        JavaType contentType = new JavaType();

        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyTypeWhenSameType() {
        MapType mapType = new MapType(TestClass.class);
        JavaType keyType = mapType.getKeyType();

        MapType withKeyType = mapType.withKeyType(keyType);

        assertEquals(keyType, withKeyType.getKeyType());
    }

    @Test
    public void testWithKeyTypeWhenDifferentType() {
        MapType mapType = new MapType(TestClass.class);
        JavaType keyType = new JavaType();

        MapType withKeyType = mapType.withKeyType(keyType);

        assertEquals(keyType, withKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];

        MapType mapType = new MapType(TestClass.class);

        JavaType refinedMapType = mapType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(refinedMapType);
        assertEquals(rawType, refinedMapType.getRawType());
        assertEquals(bindings, refinedMapType.getBindings());
        assertEquals(superClass, refinedMapType.getSuperClass());
        assertEquals(superInterfaces, refinedMapType.getInterfaces());
        assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
        assertEquals(mapType.getValueType(), refinedMapType.getValueType());
        assertEquals(mapType.getValueHandler(), refinedMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedMapType.getTypeHandler());
        assertEquals(mapType.isStatic(), refinedMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandlerWhenTypeHandlerExists() {
        String handler = "contentHandler";

        MapType mapType = new MapType(TestClass.class).withContentTypeHandler("typeHandler");

        MapType withContentTypeHandler = mapType.withContentTypeHandler(handler);

        assertNotNull(withContentTypeHandler);
        assertEquals(handler, withContentTypeHandler.getValueType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandlerWhenValueHandlerExists() {
        String handler = "contentHandler";

        MapType mapType = new MapType(TestClass.class).withContentValueHandler("valueHandler");

        MapType withContentValueHandler = mapType.withContentValueHandler(handler);

        assertNotNull(withContentValueHandler);
        assertEquals(handler, withContentValueHandler.getValueType().getValueHandler());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(TestClass.class);

        String result = mapType.toString();

        assertEquals("[map type; class com.fasterxml.jackson.databind.type.TestClass, null -> null]", result);
    }

    private static class TestSubclass extends MapType {
        //
    }

    private static class TestClass {
        //
    }
}