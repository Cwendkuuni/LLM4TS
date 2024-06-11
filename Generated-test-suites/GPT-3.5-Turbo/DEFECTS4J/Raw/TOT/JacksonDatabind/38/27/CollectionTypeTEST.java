import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
    }

    @Test
    public void testConstruct() {
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(String.class, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);

        assertNotSame(collectionType, newCollectionType);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertNull(newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(Integer.class, newCollectionType.getContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object newTypeHandler = new DummyTypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(newTypeHandler);

        assertNotSame(collectionType, newCollectionType);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertNull(newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(String.class, newCollectionType.getContentType());
        assertNull(newCollectionType.getValueHandler());
        assertEquals(newTypeHandler, newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object newValueHandler = new DummyValueHandler();
        CollectionType newCollectionType = collectionType.withContentValueHandler(newValueHandler);

        assertNotSame(collectionType, newCollectionType);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertNull(newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(String.class, newCollectionType.getContentType());
        assertEquals(newValueHandler, newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType newCollectionType = collectionType.withStaticTyping();

        assertNotSame(collectionType, newCollectionType);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertNull(newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(String.class, newCollectionType.getContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isAsStatic());
    }

    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(CollectionLikeType.class);
        JavaType[] superInterfaces = new JavaType[] {TypeFactory.defaultInstance().constructType(List.class)};
        CollectionType newCollectionType = collectionType.refine(LinkedList.class, bindings, superClass, superInterfaces);

        assertNotSame(collectionType, newCollectionType);
        assertEquals(LinkedList.class, newCollectionType.getRawClass());
        assertNotNull(newCollectionType.getBindings());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertArrayEquals(superInterfaces, newCollectionType.getSuperInterfaces());
        assertEquals(String.class, newCollectionType.getContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isAsStatic());
    }

    @Test
    public void testToString() {
        String toString = "[collection type; class java.util.ArrayList, contains class java.lang.String]";
        assertEquals(toString, collectionType.toString());
    }

    private static class DummyTypeHandler {
        // dummy type handler
    }

    private static class DummyValueHandler {
        // dummy value handler
    }
}