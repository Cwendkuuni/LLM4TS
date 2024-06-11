import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithInvalidArguments() {
        Class<?> listClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;

        try {
            CollectionType collectionType = CollectionType.construct(listClass, elemT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            CollectionType collectionType = CollectionType.construct(null, elemT);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        Class<?> sublistClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemT);
        CollectionType sublistType = CollectionType.construct(sublistClass, null, null, null, elemT);

        CollectionType narrowedType = collectionType._narrow(sublistClass);

        assertEquals(sublistType, narrowedType);
    }

    @Test
    public void testNarrowWithInvalidArguments() {
        Class<?> listClass = ArrayList.class;
        Class<?> sublistClass = HashSet.class;
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemT);

        try {
            CollectionType narrowedType = collectionType._narrow(HashSet.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        JavaType contentType = JavaType.construct(int[].class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType withContentType = collectionType.withContentType(contentType);

        assertEquals(contentType, withContentType.getContentType());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithContentTypeWithInvalidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        JavaType contentType = JavaType.construct(String.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        try {
            JavaType withContentType = collectionType.withContentType(contentType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandlerWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        Object typeHandler = new Object() {};

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType withTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertNotNull(withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandlerWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        Object contentTypeHandler = new Object() {};

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);

        assertNotNull(withContentTypeHandler.getContentTypeHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandlerWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        Object valueHandler = new Object() {};

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType withValueHandler = collectionType.withValueHandler(valueHandler);

        assertNotNull(withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandlerWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        Object contentValueHandler = new Object() {};

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);

        assertNotNull(withContentValueHandler.getContentValueHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTypingWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        CollectionType withStaticTyping = collectionType.withStaticTyping();

        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefineWithValidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInterfaces);

        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testRefineWithInvalidArguments() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        try {
            JavaType refinedType = collectionType.refine(null, bindings, superClass, superInterfaces);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            JavaType refinedType = collectionType.refine(listClass, null, superClass, superInterfaces);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            JavaType refinedType = collectionType.refine(listClass, bindings, null, superInterfaces);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            JavaType refinedType = collectionType.refine(listClass, bindings, superClass, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}