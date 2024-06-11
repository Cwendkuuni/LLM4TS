
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings;
        JavaType superClass;
        JavaType[] superInts;
        JavaType elemT;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(MockType.class);
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertNotNull(newCollectionType);
        assertEquals(contentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new MockHandler();
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withTypeHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(handler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new MockHandler();
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(handler, newCollectionType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new MockHandler();
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withValueHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(handler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new MockHandler();
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withContentValueHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(handler, newCollectionType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(CollectionType.class);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertTrue(newCollectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings;
        JavaType superClass;
        JavaType[] superInts;
        JavaType elemT;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);
        Class<?> newRawType = MockType.class;
        TypeBindings newBindings;
        JavaType newSuperClass;
        JavaType[] newSuperInts;
        CollectionType newCollectionType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInts);
        assertNotNull(newCollectionType);
        assertEquals(newRawType, newCollectionType.getRawClass());
        assertEquals(newBindings, newCollectionType.getBindings());
        assertEquals(newSuperClass, newCollectionType.getSuperClass());
        assertEquals(newSuperInts, newCollectionType.getInterfaces());
        assertEquals(elemT, newCollectionType.getContentType());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(CollectionType.class);
        String expected = "[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains null]";
        assertEquals(expected, collectionType.toString());
    }
}
