import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(), null, null, String.class);
    }

    @Test
    public void testConstruct() {
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(String.class, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isAsStatic());
    }

    @Test
    public void testConstructDeprecated() {
        CollectionType deprecatedCollectionType = CollectionType.construct(List.class, String.class);
        assertEquals(List.class, deprecatedCollectionType.getRawClass());
        assertNull(deprecatedCollectionType.getBindings());
        assertNull(deprecatedCollectionType.getSuperClass());
        assertNull(deprecatedCollectionType.getSuperInterfaces());
        assertEquals(String.class, deprecatedCollectionType.getElementType());
        assertNull(deprecatedCollectionType.getValueHandler());
        assertNull(deprecatedCollectionType.getTypeHandler());
        assertFalse(deprecatedCollectionType.isAsStatic());
    }

    @Test
    public void testNarrow() {
        CollectionType narrowedType = (CollectionType) collectionType.narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertEquals(String.class, narrowedType.getElementType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType updatedType = (CollectionType) collectionType.withContentType(newContentType);
        assertEquals(Integer.class, updatedType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new MyTypeHandler();
        CollectionType updatedType = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, updatedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new MyTypeHandler();
        CollectionType updatedType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, updatedType.getElementType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new MyValueHandler();
        CollectionType updatedType = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, updatedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new MyValueHandler();
        CollectionType updatedType = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, updatedType.getElementType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType updatedType = collectionType.withStaticTyping();
        assertTrue(updatedType.isAsStatic());
    }

    @Test
    public void testRefine() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(HashMap.class);
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(KeyType.class, TypeFactory.defaultInstance().constructType(String.class));
        CollectionType refinedType = (CollectionType) collectionType.refine(ArrayList.class, bindings, TypeFactory.defaultInstance().constructType(Collection.class), null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(TypeFactory.defaultInstance().constructType(Collection.class), refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
        assertEquals(elementType, refinedType.getElementType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isAsStatic());
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class " + List.class.getName() + ", contains " + String.class + "]";
        assertEquals(expected, collectionType.toString());
    }

    private static class MyTypeHandler {
        // implementation omitted
    }

    private static class MyValueHandler {
        // implementation omitted
    }
}