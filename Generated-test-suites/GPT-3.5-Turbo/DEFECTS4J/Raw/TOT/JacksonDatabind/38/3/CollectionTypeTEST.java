import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        // typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT);
        assertNotNull(collectionType);

        // edge case: rawType is null
        CollectionType collectionType2 = CollectionType.construct(null, TypeBindings.emptyBindings(), null, null, JavaType.INT);
        assertNull(collectionType2);
    }

    @Test
    public void testNarrow() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        JavaType narrowedType = collectionType._narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());

        // edge case: subclass is null
        JavaType narrowedType2 = collectionType._narrow(null);
        assertEquals(ArrayList.class, narrowedType2.getRawClass());
    }

    @Test
    public void testWithContentType() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        JavaType contentType = JavaType.STRING;
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertEquals(contentType, newCollectionType.getContentType());

        // edge case: contentType is the same as the original _elementType
        CollectionType newCollectionType2 = collectionType.withContentType(JavaType.INT);
        assertEquals(collectionType, newCollectionType2);
    }

    @Test
    public void testWithTypeHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        Object typeHandler = new TypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newCollectionType.getTypeHandler());

        // edge case: typeHandler is null
        CollectionType newCollectionType2 = collectionType.withTypeHandler(null);
        assertNull(newCollectionType2.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        Object typeHandler = new TypeHandler();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, newCollectionType.getElementType().getTypeHandler());

        // edge case: typeHandler is null
        CollectionType newCollectionType2 = collectionType.withContentTypeHandler(null);
        assertNull(newCollectionType2.getElementType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        Object valueHandler = new ValueHandler();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newCollectionType.getValueHandler());

        // edge case: valueHandler is null
        CollectionType newCollectionType2 = collectionType.withValueHandler(null);
        assertNull(newCollectionType2.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        Object valueHandler = new ValueHandler();
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, newCollectionType.getElementType().getValueHandler());

        // edge case: valueHandler is null
        CollectionType newCollectionType2 = collectionType.withContentValueHandler(null);
        assertNull(newCollectionType2.getElementType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertTrue(newCollectionType.isStaticTyping());

        // edge case: _asStatic is already true
        CollectionType newCollectionType2 = newCollectionType.withStaticTyping();
        assertEquals(newCollectionType, newCollectionType2);
    }

    @Test
    public void testRefine() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        JavaType superClass = JavaType.OBJECT;
        JavaType[] superInterfaces = new JavaType[]{JavaType.SERIALIZABLE};
        CollectionType newCollectionType = collectionType.refine(Collection.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertArrayEquals(superInterfaces, newCollectionType.getSuperInterfaces());

        // edge case: rawType is null
        CollectionType newCollectionType2 = collectionType.refine(null, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNull(newCollectionType2.getRawClass());
    }

    @Test
    public void testToString() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);
        String expected = "[collection type; class java.util.ArrayList, contains INT]";
        assertEquals(expected, collectionType.toString());
    }
}