import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperclass());
        assertArrayEquals(new JavaType[0], collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        Class<ArrayList<Integer>> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperclass());
        assertArrayEquals(new JavaType[0], collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(null, bindings, superClass, superInterfaces, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullBindings() {
        Class<List<Integer>> listClass = List.class;
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, null, superClass, superInterfaces, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullSuperClass() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, null, superInterfaces, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullInterfaceTypes() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, JavaType.constructRawType(Integer.class));
    }

    @Test
    public void testDeprecatedConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, elemT);

        assertEquals(listClass, collectionType.getRawClass());
        assertNull(collectionType.getTypeBindings());
        assertEquals(JavaType.constructRawListType(Integer.class), collectionType.getSuperclass());
        assertArrayEquals(new JavaType[0], collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<String>> arrayListClass = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(arrayListClass, null, null, null, JavaType.constructRawType(String.class));

        CollectionType narrowedType = collectionType.narrow(LinkedList.class);

        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertSame(collectionType.getTypeBindings(), narrowedType.getTypeBindings());
        assertSame(collectionType.getSuperclass(), narrowedType.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class), narrowedType.getElementType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType withContentType = collectionType.withContentType(contentType);

        assertEquals(listClass, withContentType.getRawClass());
        assertSame(collectionType.getTypeBindings(), withContentType.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withContentType.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withContentType.getInterfaces());
        assertSame(contentType.getElementType(), withContentType.getElementType());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        Object typeHandler = new Object() {};
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertEquals(listClass, withTypeHandler.getRawClass());
        assertSame(collectionType.getTypeBindings(), withTypeHandler.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withTypeHandler.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withTypeHandler.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class), withTypeHandler.getElementType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getValueHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        Object contentTypeHandler = new Object() {};
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);

        assertEquals(listClass, withContentTypeHandler.getRawClass());
        assertSame(collectionType.getTypeBindings(), withContentTypeHandler.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withContentTypeHandler.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withContentTypeHandler.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class).withTypeHandler(contentTypeHandler), withContentTypeHandler.getElementType());
        assertNull(withContentTypeHandler.getValueHandler());
        assertNull(withContentTypeHandler.getTypeHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        Object valueHandler = new Object() {};
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);

        assertEquals(listClass, withValueHandler.getRawClass());
        assertSame(collectionType.getTypeBindings(), withValueHandler.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withValueHandler.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withValueHandler.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class), withValueHandler.getElementType());
        assertNull(withValueHandler.getValueHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        Object contentValueHandler = new Object() {};
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);

        assertEquals(listClass, withContentValueHandler.getRawClass());
        assertSame(collectionType.getTypeBindings(), withContentValueHandler.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withContentValueHandler.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withContentValueHandler.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class).withValueHandler(contentValueHandler), withContentValueHandler.getElementType());
        assertNull(withContentValueHandler.getValueHandler());
        assertNull(withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.constructRawType(String.class));

        CollectionType withStaticTyping = collectionType.withStaticTyping();

        assertEquals(listClass, withStaticTyping.getRawClass());
        assertSame(collectionType.getTypeBindings(), withStaticTyping.getTypeBindings());
        assertSame(collectionType.getSuperclass(), withStaticTyping.getSuperclass());
        assertArrayEquals(collectionType.getInterfaces(), withStaticTyping.getInterfaces());
        assertEquals(JavaType.constructRawType(String.class), withStaticTyping.getElementType());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<LinkedList<Integer>> linkedListClass = LinkedList.class;
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, JavaType.constructRawType(Integer.class));

        CollectionType refinedType = collectionType.refine(linkedListClass, null, null, null);

        assertEquals(linkedListClass, refinedType.getRawClass());
        assertNull(refinedType.getTypeBindings());
        assertSame(JavaType.constructRawListType(Integer.class), refinedType.getSuperclass());
        assertArrayEquals(new JavaType[0], refinedType.getInterfaces());
        assertEquals(JavaType.constructRawType(Integer.class), refinedType.getElementType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }
}