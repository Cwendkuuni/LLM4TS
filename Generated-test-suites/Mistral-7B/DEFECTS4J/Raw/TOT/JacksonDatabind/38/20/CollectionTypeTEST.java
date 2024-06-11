import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
        assertNotNull(collectionType);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecatedWithValidArguments() {
        Class<List<Integer>> listClass = java.util.List.class;
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, elemT);
        assertNotNull(collectionType);
        assertEquals(listClass, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertFalse(collectionType.isStatic());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidArguments() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.lang.String.class; // wrong superclass
        JavaType[] superInterfaces = new JavaType[]{java.util.Map.class}; // wrong superinterface
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<Integer>> arrayListClass = java.util.ArrayList.class;
        CollectionType listType = CollectionType.construct(java.util.List.class, null, null, null, JavaType.constructRawListType(Integer.class));
        CollectionType arrayListType = listType.narrow(arrayListClass);
        assertNotNull(arrayListType);
        assertEquals(arrayListClass, arrayListType.getRawClass());
        assertEquals(listType.getBindings(), arrayListType.getBindings());
        assertEquals(listType.getSuperClass(), arrayListType.getSuperClass());
        assertEquals(listType.getSuperInterfaces(), arrayListType.getSuperInterfaces());
        assertEquals(listType.getElementType(), arrayListType.getElementType());
        assertFalse(arrayListType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        JavaType contentType = JavaType.constructRawListType(Long.class);
        CollectionType newListType = listType.withContentType(contentType);
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(contentType, newListType.getElementType());
        assertFalse(newListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        Object typeHandler = new Object();
        CollectionType newListType
                = listType.withTypeHandler(typeHandler);
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(elemT, newListType.getElementType());
        assertEquals(typeHandler, newListType.getTypeHandler());
        assertFalse(newListType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        Object contentTypeHandler = new Object();
        Object typeHandler = new Object();
        CollectionType newListType
                = listType.withContentTypeHandler(contentTypeHandler).withTypeHandler(typeHandler);
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(elemT.withTypeHandler(contentTypeHandler), newListType.getElementType());
        assertEquals(typeHandler, newListType.getTypeHandler());
        assertFalse(newListType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        Object valueHandler = new Object();
        CollectionType newListType
                = listType.withValueHandler(valueHandler);
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(elemT, newListType.getElementType());
        assertEquals(valueHandler, newListType.getValueHandler());
        assertFalse(newListType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        Object contentValueHandler = new Object();
        Object valueHandler = new Object();
        CollectionType newListType
                = listType.withContentValueHandler(contentValueHandler).withValueHandler(valueHandler);
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(elemT.withValueHandler(contentValueHandler), newListType.getElementType());
        assertEquals(valueHandler, newListType.getValueHandler());
        assertFalse(newListType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = java.util.Collection.class;
        JavaType[] superInterfaces = new JavaType[]{java.lang.Iterable.class};
        JavaType elemT = JavaType.constructRawListType(Integer.class);
        CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType newListType = listType.withStaticTyping();
        assertNotNull(newListType);
        assertEquals(listClass, newListType.getRawClass());
        assertEquals(bindings, newListType.getBindings());
        assertEquals(superClass, newListType.getSuperClass());
        assertEquals(superInterfaces, newListType.getSuperInterfaces());
        assertEquals(elemT, newListType.getElementType());
        assertTrue(newListType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<String>> arrayListClass = java.util.ArrayList.class;
        JavaType rawListType = JavaType.constructRawListType(String.class);
        CollectionType listType = new CollectionType(rawListType, null, null, null, JavaType.constructRawListType(String.class), null, null, false);
        CollectionType newListType = listType.refine(arrayListClass.getTypeParameters()[0].getRawType(), null, null, null);
        assertNotNull(newListType);
        assertEquals(arrayListClass, newListType.getRawClass());
        assertEquals(null, newListType.getBindings());
        assertEquals(null, newListType.getSuperClass());
        assertEquals(null, newListType.getSuperInterfaces());
        assertEquals(JavaType.constructRawType(arrayListClass.getTypeParameters()[0].getRawType()), newListType.getElementType());
        assertFalse(newListType.isStatic());
    }
}