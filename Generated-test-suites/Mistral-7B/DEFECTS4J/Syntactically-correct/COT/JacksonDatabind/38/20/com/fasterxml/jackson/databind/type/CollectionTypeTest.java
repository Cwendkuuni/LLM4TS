import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertEquals(false, collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemT = JavaType.construct(int.class, null);
        try {
            CollectionType collectionType = CollectionType.construct(listClass, elemT);
            fail("Constructor should be deprecated");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowDeprecated() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(arrayListClass, null, null, null, JavaType.construct(int.class, null));
        try {
            JavaType narrowedType = collectionType._narrow(LinkedList.class);
            fail("_narrow method should be deprecated");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int[].class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType withContentType = collectionType.withContentType(JavaType.construct(int.class, null));
        assertEquals(listClass, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getTypeBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertEquals(superInts, withContentType.getSuperInterfaces());
        assertEquals(JavaType.construct(int.class, null), withContentType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        Object typeHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals(listClass, withTypeHandler.getRawClass());
        assertEquals(bindings, withTypeHandler.getTypeBindings());
        assertEquals(superClass, withTypeHandler.getSuperClass());
        assertEquals(superInts, withTypeHandler.getSuperInterfaces());
        assertEquals(elemT, withTypeHandler.getElementType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        Object contentTypeHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(listClass, withContentTypeHandler.getRawClass());
        assertEquals(bindings, withContentTypeHandler.getTypeBindings());
        assertEquals(superClass, withContentTypeHandler.getSuperClass());
        assertEquals(superInts, withContentTypeHandler.getSuperInterfaces());
        assertEquals(elemT.withTypeHandler(contentTypeHandler), withContentTypeHandler.getElementType());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        Object valueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals(listClass, withValueHandler.getRawClass());
        assertEquals(bindings, withValueHandler.getTypeBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertEquals(superInts, withValueHandler.getSuperInterfaces());
        assertEquals(elemT, withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        Object contentValueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(listClass, withContentValueHandler.getRawClass());
        assertEquals(bindings, withContentValueHandler.getTypeBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertEquals(superInts, withContentValueHandler.getSuperInterfaces());
        assertEquals(elemT.withValueHandler(contentValueHandler), withContentValueHandler.getElementType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(listClass, withStaticTyping.getRawClass());
        assertEquals(bindings, withStaticTyping.getTypeBindings());
        assertEquals(superClass, withStaticTyping.getSuperClass());
        assertEquals(superInts, withStaticTyping.getSuperInterfaces());
        assertEquals(elemT, withStaticTyping.getElementType());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType refinedType = collectionType.refine(ArrayList.class, bindings, null, null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getTypeBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInts, refinedType.getSuperInterfaces());
        assertEquals(elemT, refinedType.getElementType());
    }

    @Test
    public void testToString() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(int.class, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        String result = collectionType.toString();
        String expected = "[collection type; class " + listClass.getName() + ", contains " + elemT + "]";
        assertEquals(expected, result);
    }
}