import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test construction
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstruct_withContentType() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withContentType method
        JavaType contentType = null;
        JavaType result = collectionType.withContentType(contentType);
        assertEquals(collectionType, result);

        contentType = new JavaType();
        result = collectionType.withContentType(contentType);
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(contentType, result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testConstruct_withTypeHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withTypeHandler method
        Object h = null;
        CollectionType result = collectionType.withTypeHandler(h);
        assertEquals(collectionType, result);

        h = new Object();
        result = collectionType.withTypeHandler(h);
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(elemT, result.getContentType());
        assertNull(result.getValueHandler());
        assertEquals(h, result.getTypeHandler());
        assertFalse(result.isStatic());
    }
    
    @Test
    public void testConstruct_withContentTypeHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withContentTypeHandler method
        Object h = null;
        CollectionType result = collectionType.withContentTypeHandler(h);
        assertEquals(collectionType, result);

        h = new Object();
        result = collectionType.withContentTypeHandler(h);
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertNotEquals(elemT, result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testConstruct_withValueHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withValueHandler method
        Object h = null;
        CollectionType result = collectionType.withValueHandler(h);
        assertEquals(collectionType, result);

        h = new Object();
        result = collectionType.withValueHandler(h);
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(elemT, result.getContentType());
        assertEquals(h, result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testConstruct_withContentValueHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withContentValueHandler method
        Object h = null;
        CollectionType result = collectionType.withContentValueHandler(h);
        assertEquals(collectionType, result);

        h = new Object();
        result = collectionType.withContentValueHandler(h);
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(elemT, result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testConstruct_withStaticTyping() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test withStaticTyping method
        CollectionType result = collectionType.withStaticTyping();
        assertEquals(collectionType, result);

        result = collectionType.withStaticTyping();
        assertNotEquals(collectionType, result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(elemT, result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test refine method
        Class<?> newRawType = null;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newSuperInts = null;
        JavaType newElemT = null;
        JavaType result = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInts);
        assertNotEquals(collectionType, result);
        assertEquals(newRawType, result.getRawClass());
        assertEquals(newBindings, result.getBindings());
        assertEquals(newSuperClass, result.getSuperClass());
        assertArrayEquals(newSuperInts, result.getInterfaces());
        assertEquals(newElemT, result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isStatic());
    }

    @Test
    public void testToString() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT,
                null, null, false);

        // Test toString method
        String expected = "[collection type; class null, contains null]";
        String result = collectionType.toString();
        assertEquals(expected, result);
    }
}