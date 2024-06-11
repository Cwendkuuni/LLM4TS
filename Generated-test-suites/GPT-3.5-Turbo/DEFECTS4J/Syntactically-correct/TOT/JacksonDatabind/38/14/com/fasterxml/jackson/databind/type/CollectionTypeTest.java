import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, null, null, null, false);
    }

    @Test
    public void testConstruct_withRawTypeAndBindingsAndSuperClassAndSuperInterfacesAndElementType() {
        CollectionType constructedType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, null);
        assertEquals(collectionType, constructedType);
    }

    @Test
    public void testWithContentType_withSameContentType() {
        CollectionType modifiedType = collectionType.withContentType(collectionType.getElementType());
        assertEquals(collectionType, modifiedType);
    }

    @Test
    public void testWithContentType_withDifferentContentType() {
        JavaType contentType = new SimpleType(String.class);
        CollectionType modifiedType = collectionType.withContentType(contentType);
        assertEquals(contentType, modifiedType.getElementType());
    }

    @Test
    public void testWithTypeHandler_withTypeHandler() {
        Object typeHandler = new TypeHandler();
        CollectionType modifiedType = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler_withContentTypeHandler() {
        Object contentTypeHandler = new TypeHandler();
        CollectionType modifiedType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, modifiedType.getElementType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler_withValueHandler() {
        Object valueHandler = new ValueHandler();
        CollectionType modifiedType = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler_withContentValueHandler() {
        Object contentValueHandler = new ValueHandler();
        CollectionType modifiedType = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, modifiedType.getElementType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping_withStaticTyping() {
        CollectionType modifiedType = collectionType.withStaticTyping();
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testRefine_withRawTypeAndBindingsAndSuperClassAndSuperInterfaces() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Collection.class);
        JavaType[] superInterfaces = {new SimpleType(Serializable.class)};
        CollectionType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(collectionType.getElementType(), refinedType.getElementType());
        assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), refinedType.isStaticTyping());
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class " + ArrayList.class.getName() + ", contains " + collectionType.getElementType() + "]";
        assertEquals(expected, collectionType.toString());
    }

    private static class TypeHandler {
        // implementation for testing purposes
    }

    private static class ValueHandler {
        // implementation for testing purposes
    }

}