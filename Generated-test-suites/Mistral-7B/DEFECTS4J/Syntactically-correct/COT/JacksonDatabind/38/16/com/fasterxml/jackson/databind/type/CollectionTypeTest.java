import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(collType, bindings, superClass, superInts, elemT);
        assertEquals(collType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperclass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testDeprecatedConstruct() {
        Class<?> collType = Collection.class;
        JavaType elemT = String.class;

        CollectionType collectionType = CollectionType.construct(collType, elemT);

        assertEquals(collType, collectionType.getRawClass());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperclass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedNarrowMethod() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, String.class);
        collectionType._narrow(LinkedList.class);
    }

    @Test
    public void testWithContentType() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        JavaType contentType = Integer.class;

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newCollectionType = collectionType.withContentType(contentType);

        assertEquals(collType, newCollectionType.getRawClass());
        assertNotSame(elemT, newCollectionType.getElementType());
        assertSame(contentType, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        Object typeHandler = new Object();

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newCollectionType = collectionType.withTypeHandler(typeHandler);

        assertEquals(collType, newCollectionType.getRawClass());
        assertNotSame(null, newCollectionType.getTypeHandler());
        assertSame(elemT, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        Object contentTypeHandler = new Object();

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);

        assertEquals(collType, newCollectionType.getRawClass());
        assertNotSame(null, newCollectionType.getContentTypeHandler());
        assertSame(elemT, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        Object valueHandler = new Object();

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newCollectionType = collectionType.withValueHandler(valueHandler);

        assertEquals(collType, newCollectionType.getRawClass());
        assertNotSame(null, newCollectionType.getValueHandler());
        assertSame(elemT, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;
        Object contentValueHandler = new Object();

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);

        assertEquals(collType, newCollectionType.getRawClass());
        assertNotSame(null, newCollectionType.getContentValueHandler());
        assertSame(elemT, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> collType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Array.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(collType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();

        assertEquals(collType, newCollectionType.getRawClass());
        assertTrue(newCollectionType._asStatic);
        assertSame(elemT, newCollectionType.getElementType());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertEquals(superClass, newCollectionType.getSuperclass());
        assertArrayEquals(superInts, newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.refine(LinkedList.class, bindings, null, null);

        assertEquals(LinkedList.class, newCollectionType.getRawClass());
        assertSame(bindings, newCollectionType.getTypeBindings());
        assertSame(elemT, newCollectionType.getElementType());
        assertNull(newCollectionType.getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
    }
}