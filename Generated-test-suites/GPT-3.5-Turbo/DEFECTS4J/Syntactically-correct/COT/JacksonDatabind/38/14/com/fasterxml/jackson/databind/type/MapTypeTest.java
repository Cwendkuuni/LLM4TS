
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstructWithBindings() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(MapTypeTest.class, bindings, superClass, superInts, keyT, valueT);

        assertEquals(MapTypeTest.class, mapType.getRawType());
        assertSame(bindings, mapType.getBindings());
        assertSame(superClass, mapType.getSuperClass());
        assertSame(superInts, mapType.getInterfaces());
        assertSame(keyT, mapType.getKeyType());
        assertSame(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testDeprecatedConstruct() {
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(MapTypeTest.class, keyT, valueT);

        assertEquals(MapTypeTest.class, mapType.getRawType());
        assertNull(mapType.getBindings());
        assertNotNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertSame(keyT, mapType.getKeyType());
        assertSame(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType narrowedType = (MapType) mapType._narrow(MapTypeTest.class);

        assertEquals(MapTypeTest.class, narrowedType.getRawType());
        assertSame(mapType.getBindings(), mapType.getBindings());
        assertSame(mapType.getSuperClass(), mapType.getSuperClass());
        assertSame(mapType.getInterfaces(), mapType.getInterfaces());
        assertSame(mapType.getKeyType(), mapType.getKeyType());
        assertSame(mapType.getValueType(), mapType.getValueType());
        assertSame(mapType.getValueHandler(), mapType.getValueHandler());
        assertSame(mapType.getTypeHandler(), mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withTypeHandlerType = mapType.withTypeHandler(typeHandler);

        assertEquals(MapTypeTest.class, withTypeHandlerType.getRawType());
        assertSame(mapType.getBindings(), withTypeHandlerType.getBindings());
        assertSame(mapType.getSuperClass(), withTypeHandlerType.getSuperClass());
        assertSame(mapType.getInterfaces(), withTypeHandlerType.getInterfaces());
        assertSame(mapType.getKeyType(), withTypeHandlerType.getKeyType());
        assertSame(mapType.getValueType(), withTypeHandlerType.getValueType());
        assertSame(mapType.getValueHandler(), withTypeHandlerType.getValueHandler());
        assertSame(typeHandler, withTypeHandlerType.getTypeHandler());
        assertFalse(withTypeHandlerType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentValueHandler = new Object();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withContentTypeHandlerType = mapType.withContentTypeHandler(contentValueHandler);

        assertEquals(MapTypeTest.class, withContentTypeHandlerType.getRawType());
        assertSame(mapType.getBindings(), withContentTypeHandlerType.getBindings());
        assertSame(mapType.getSuperClass(), withContentTypeHandlerType.getSuperClass());
        assertSame(mapType.getInterfaces(), withContentTypeHandlerType.getInterfaces());
        assertSame(mapType.getKeyType(), withContentTypeHandlerType.getKeyType());
        assertSame(mapType.getValueType(), withContentTypeHandlerType.getValueType());
        assertSame(mapType.getValueHandler(), withContentTypeHandlerType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withContentTypeHandlerType.getTypeHandler());
        assertFalse(withContentTypeHandlerType.isStatic());
    }
    
    // Additional test cases for edge cases and exceptions

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withStaticTypingType = mapType.withStaticTyping();

        assertEquals(MapTypeTest.class, withStaticTypingType.getRawType());
        assertSame(mapType.getBindings(), withStaticTypingType.getBindings());
        assertSame(mapType.getSuperClass(), withStaticTypingType.getSuperClass());
        assertSame(mapType.getInterfaces(), withStaticTypingType.getInterfaces());
        assertNotSame(mapType.getKeyType(), withStaticTypingType.getKeyType());
        assertNotSame(mapType.getValueType(), withStaticTypingType.getValueType());
        assertSame(mapType.getValueHandler(), withStaticTypingType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withStaticTypingType.getTypeHandler());
        assertTrue(withStaticTypingType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withContentTypeType = mapType.withContentType(contentType);

        assertEquals(MapTypeTest.class, withContentTypeType.getRawType());
        assertSame(mapType.getBindings(), withContentTypeType.getBindings());
        assertSame(mapType.getSuperClass(), withContentTypeType.getSuperClass());
        assertSame(mapType.getInterfaces(), withContentTypeType.getInterfaces());
        assertSame(mapType.getKeyType(), withContentTypeType.getKeyType());
        assertSame(contentType, withContentTypeType.getValueType());
        assertSame(mapType.getValueHandler(), withContentTypeType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withContentTypeType.getTypeHandler());
        assertFalse(withContentTypeType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withKeyTypeType = mapType.withKeyType(keyType);

        assertEquals(MapTypeTest.class, withKeyTypeType.getRawType());
        assertSame(mapType.getBindings(), withKeyTypeType.getBindings());
        assertSame(mapType.getSuperClass(), withKeyTypeType.getSuperClass());
        assertSame(mapType.getInterfaces(), withKeyTypeType.getInterfaces());
        assertSame(keyType, withKeyTypeType.getKeyType());
        assertSame(mapType.getValueType(), withKeyTypeType.getValueType());
        assertSame(mapType.getValueHandler(), withKeyTypeType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withKeyTypeType.getTypeHandler());
        assertFalse(withKeyTypeType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        JavaType refinedType = mapType.refine(MapTypeTest.class, null, null, null);

        assertEquals(MapTypeTest.class, refinedType.getRawType());
        assertSame(mapType.getBindings(), refinedType.getBindings());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getInterfaces());
        assertSame(mapType.getKeyType(), refinedType.getKeyType());
        assertSame(mapType.getValueType(), refinedType.getValueType());
        assertSame(mapType.getValueHandler(), refinedType.getValueHandler());
        assertSame(mapType.getTypeHandler(), refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withKeyTypeHandlerType = mapType.withKeyTypeHandler(keyTypeHandler);

        assertEquals(MapTypeTest.class, withKeyTypeHandlerType.getRawType());
        assertSame(mapType.getBindings(), withKeyTypeHandlerType.getBindings());
        assertSame(mapType.getSuperClass(), withKeyTypeHandlerType.getSuperClass());
        assertSame(mapType.getInterfaces(), withKeyTypeHandlerType.getInterfaces());
        assertSame(keyTypeHandler, withKeyTypeHandlerType.getKeyType().getTypeHandler());
        assertSame(mapType.getValueType(), withKeyTypeHandlerType.getValueType());
        assertSame(mapType.getValueHandler(), withKeyTypeHandlerType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withKeyTypeHandlerType.getTypeHandler());
        assertFalse(withKeyTypeHandlerType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();

        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        MapType withKeyValueHandlerType = mapType.withKeyValueHandler(keyValueHandler);

        assertEquals(MapTypeTest.class, withKeyValueHandlerType.getRawType());
        assertSame(mapType.getBindings(), withKeyValueHandlerType.getBindings());
        assertSame(mapType.getSuperClass(), withKeyValueHandlerType.getSuperClass());
        assertSame(mapType.getInterfaces(), withKeyValueHandlerType.getInterfaces());
        assertSame(mapType.getKeyType(), withKeyValueHandlerType.getKeyType());
        assertSame(mapType.getValueType().getValueHandler(), withKeyValueHandlerType.getValueType().getValueHandler());
        assertSame(mapType.getValueHandler(), withKeyValueHandlerType.getValueHandler());
        assertSame(mapType.getTypeHandler(), withKeyValueHandlerType.getTypeHandler());
        assertFalse(withKeyValueHandlerType.isStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(MapTypeTest.class, null, null, null, null, null, null, null, false);
        String expectedToString = "[map type; class " + MapTypeTest.class.getName() + ", null -> null]";

        assertEquals(expectedToString, mapType.toString());
    }
}

