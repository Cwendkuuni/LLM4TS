import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = { new SimpleType(Number.class) };
        JavaType elemT = new SimpleType(String.class);

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testDeprecatedConstruct() {
        Class<?> rawType = Set.class;
        JavaType elemT = new SimpleType(Integer.class);

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNotNull(collectionType.getSuperClass()); // Should be _bogusSuperClass
        assertNull(collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Serializable.class) };
        JavaType elemT = new SimpleType(Double.class);

        CollectionType collectionType = new CollectionType(superclass, bindings, superInts, elemT, null, null, false);
        JavaType narrowedType = collectionType._narrow(subclass);

        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertArrayEquals(superInterfaces, narrowedType.getInterfaces());
        assertEquals(elemT, narrowedType.getContentType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Cloneable.class) };
        JavaType contentType = new SimpleType(Long.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = true;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, null, valueHandler, typeHandler, asStatic);
        JavaType withContentType = collectionType.withContentType(contentType);

        assertEquals(rawType, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertArrayEquals(superInterfaces, withContentType.getInterfaces());
        assertEquals(contentType, withContentType.getContentType());
        assertEquals(valueHandler, withContentType.getValueHandler());
        assertEquals(typeHandler, withContentType.getTypeHandler());
        assertEquals(asStatic, withContentType.isStaticTyping());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = HashSet.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Cloneable.class) };
        JavaType elemT = new SimpleType(Character.class);
        Object valueHandler = null;
        Object typeHandler = new TypeHandler();
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, null, asStatic);
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertEquals(rawType, withTypeHandler.getRawClass());
        assertEquals(bindings, withTypeHandler.getBindings());
        assertEquals(superClass, withTypeHandler.getSuperClass());
        assertArrayEquals(superInterfaces, withTypeHandler.getInterfaces());
        assertEquals(elemT, withTypeHandler.getContentType());
        assertEquals(valueHandler, withTypeHandler.getValueHandler());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(asStatic, withTypeHandler.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = TreeSet.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Serializable.class) };
        JavaType elemT = new SimpleType(Byte.class);
        Object valueHandler = null;
        Object typeHandler = new TypeHandler();
        boolean asStatic = true;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);

        assertEquals(rawType, withContentTypeHandler.getRawClass());
        assertEquals(bindings, withContentTypeHandler.getBindings());
        assertEquals(superClass, withContentTypeHandler.getSuperClass());
        assertArrayEquals(superInterfaces, withContentTypeHandler.getInterfaces());
        assertEquals(elemT.withTypeHandler(typeHandler), withContentTypeHandler.getContentType());
        assertEquals(valueHandler, withContentTypeHandler.getValueHandler());
        assertEquals(typeHandler, withContentTypeHandler.getTypeHandler());
        assertEquals(asStatic, withContentTypeHandler.isStaticTyping());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Vector.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Iterable.class) };
        JavaType elemT = new SimpleType(Boolean.class);
        Object valueHandler = new ValueHandler();
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, typeHandler, asStatic);
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);

        assertEquals(rawType, withValueHandler.getRawClass());
        assertEquals(bindings, withValueHandler.getBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertArrayEquals(superInterfaces, withValueHandler.getInterfaces());
        assertEquals(elemT, withValueHandler.getContentType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(typeHandler, withValueHandler.getTypeHandler());
        assertEquals(asStatic, withValueHandler.isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = CopyOnWriteArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(RandomAccess.class) };
        JavaType elemT = new SimpleType(Float.class);
        Object valueHandler = new ValueHandler();
        Object typeHandler = new TypeHandler();
        boolean asStatic = true;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, false);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);

        assertEquals(rawType, withContentValueHandler.getRawClass());
        assertEquals(bindings, withContentValueHandler.getBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertArrayEquals(superInterfaces, withContentValueHandler.getInterfaces());
        assertEquals(elemT.withValueHandler(valueHandler), withContentValueHandler.getContentType());
        assertEquals(valueHandler, withContentValueHandler.getValueHandler());
        assertEquals(typeHandler, withContentValueHandler.getTypeHandler());
        assertEquals(asStatic, withContentValueHandler.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Stack.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Cloneable.class) };
        JavaType elemT = new SimpleType(Integer.class);
        Object valueHandler = new ValueHandler();
        Object typeHandler = new TypeHandler();
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        CollectionType withStaticTyping = collectionType.withStaticTyping();

        assertEquals(rawType, withStaticTyping.getRawClass());
        assertEquals(bindings, withStaticTyping.getBindings());
        assertEquals(superClass, withStaticTyping.getSuperClass());
        assertArrayEquals(superInterfaces, withStaticTyping.getInterfaces());
        assertEquals(elemT.withStaticTyping(), withStaticTyping.getContentType());
        assertEquals(valueHandler, withStaticTyping.getValueHandler());
        assertEquals(typeHandler, withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = PriorityQueue.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(Comparable.class) };
        JavaType elemT = new SimpleType(Character.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, null, null, false);
        JavaType refinedType = collectionType.refine(List.class, bindings, superClass, superInterfaces);

        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getInterfaces());
        assertNull(refinedType.getContentType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStaticTyping());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Map.class;
        JavaType elemT = new SimpleType(String.class);

        CollectionType collectionType = new CollectionType(rawType, null, null, null, elemT, null, null, false);
        String expectedResult = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expectedResult, collectionType.toString());
    }
}