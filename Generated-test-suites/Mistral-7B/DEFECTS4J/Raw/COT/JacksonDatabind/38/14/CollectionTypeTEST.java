import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstructWithArguments() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testConstructDeprecatedWithArguments() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemT = JavaType.construct(Integer.class);
        try {
            CollectionType collectionType = CollectionType.construct(listClass, elemT);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowMethod() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType listType = CollectionType.construct(List.class, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType narrowedType = listType._narrow(arrayListClass);
        assertNotNull(narrowedType);
        assertEquals(arrayListClass, narrowedType.getRawClass());
        assertEquals(listType.getBindings(), narrowedType.getBindings());
        assertEquals(listType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(listType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        assertEquals(listType.getElementType(), narrowedType.getElementType());
    }

    @Test(expected = DeprecationException.class)
    public void testDeprecatedNarrowMethod() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType listType = CollectionType.construct(List.class, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType narrowedType = listType.withTypeHandler(null)._narrow(arrayListClass);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType contentType = JavaType.construct(String.class);
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNotSame(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(contentType, newCollectionType.getElementType());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNotSame(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType contentType = JavaType.construct(String.class);
        Object contentTypeHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNotSame(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(contentType, newCollectionType.getContentType());
        assertEquals(contentTypeHandler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        Object valueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNotSame(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        Object contentValueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNotSame(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(contentValueHandler, newCollectionType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<List<Integer>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], JavaType.construct(Integer.class));
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertTrue(newCollectionType.isStaticTyping());
    }

    @Test
    public void testRefineMethod() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = CollectionType.construct(List.class, null, null, new JavaType[0], elemT);
        CollectionType newCollectionType = collectionType.refine(arrayListClass, null, null, new JavaType[0]);
        assertNotNull(newCollectionType);
        assertEquals(arrayListClass, newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(elemT, newCollectionType.getElementType());
    }
}