
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        JavaType contentType = new JavaType();
        MapType withContentType = mapType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(contentType, withContentType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        JavaType keyType = new JavaType();
        MapType withKeyType = mapType.withKeyType(keyType);
        assertNotNull(withKeyType);
        assertEquals(keyType, withKeyType.getKeyType());
    }

    @Test
    public void testWithValueType() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        JavaType valueType = new JavaType();
        MapType withValueType = mapType.withValueType(valueType);
        assertNotNull(withValueType);
        assertEquals(valueType, withValueType.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testWithValueTypeHandler() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Object valueTypeHandler = new Object();
        MapType withValueTypeHandler = mapType.withValueTypeHandler(valueTypeHandler);
        assertNotNull(withValueTypeHandler);
        assertEquals(valueTypeHandler, withValueTypeHandler.getValueTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Object contentTypeHandler = new Object();
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Object keyValueHandler = new Object();
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(withKeyValueHandler);
        assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        Class<?> subclass = MapTypeRefineTest.class;
        TypeBindings subclassBindings = new TypeBindings();
        JavaType subclassSuperClass = new JavaType();
        JavaType[] subclassSuperInterfaces = new JavaType[1];
        subclassSuperInterfaces[0] = new JavaType();
        JavaType subclassKeyType = new JavaType();
        JavaType subclassValueType = new JavaType();

        MapType refinedMapType = mapType.refine(subclass, subclassBindings, subclassSuperClass, subclassSuperInterfaces);
        assertNotNull(refinedMapType);
        assertEquals(subclass, refinedMapType.getRawType());
    }

    /**
     * Test edge case where the raw type is null
     */
    @Test
    public void testEdgeCaseRawTypeNull() {
        Class<?> rawType = null;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNull(mapType);
    }

    /**
     * Test edge case where the bindings is null
     */
    @Test
    public void testEdgeCaseBindingsNull() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
    }

    /**
     * Test exception handling when constructing a MapType with invalid parameters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidParameters() {
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType.construct(rawType, bindings, superClass, superInts, null, valueT);
    }

}
