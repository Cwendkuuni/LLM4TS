import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInts, collectionType.getInterfaces());
        assertSame(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testConstruct_Deprecated() {
        Class<?> rawType = List.class;
        JavaType elemT = new SimpleType(String.class);

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertTrue(collectionType.getSuperClass() instanceof BogusClass);
        assertNull(collectionType.getInterfaces());
        assertSame(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = ArrayList.class;

        CollectionType originalCollectionType = CollectionType.construct(List.class, new SimpleType(String.class));
        CollectionType narrowedCollectionType = originalCollectionType._narrow(subclass);

        assertEquals(subclass, narrowedCollectionType.getRawClass());
        assertEquals(originalCollectionType.getBindings(), narrowedCollectionType.getBindings());
        assertSame(originalCollectionType.getSuperClass(), narrowedCollectionType.getSuperClass());
        assertSame(originalCollectionType.getInterfaces(), narrowedCollectionType.getInterfaces());
        assertSame(originalCollectionType.getContentType(), narrowedCollectionType.getContentType());
        assertNull(narrowedCollectionType.getValueHandler());
        assertNull(narrowedCollectionType.getTypeHandler());
        assertEquals(originalCollectionType.isStaticTyping(), narrowedCollectionType.isStaticTyping());
    }

    @Test
    public void test_withContentType() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType newContentType = new SimpleType(Integer.class);
        CollectionType newCollectionType = originalCollectionType.withContentType(newContentType);

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertSame(newContentType, newCollectionType.getContentType());
        assertSame(valueHandler, newCollectionType.getValueHandler());
        assertSame(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStaticTyping());
    }

    @Test
    public void test_withTypeHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object newTypeHandler = new Object();
        CollectionType newCollectionType = originalCollectionType.withTypeHandler(newTypeHandler);

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertSame(elemT, newCollectionType.getContentType());
        assertSame(valueHandler, newCollectionType.getValueHandler());
        assertSame(newTypeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStaticTyping());
    }

    @Test
    public void test_withContentTypeHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object newTypeHandler = new Object();
        CollectionType newCollectionType = originalCollectionType.withContentTypeHandler(newTypeHandler);

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertEquals(new SimpleType(String.class, null, newTypeHandler), newCollectionType.getContentType());
        assertSame(valueHandler, newCollectionType.getValueHandler());
        assertSame(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStaticTyping());
    }

    @Test
    public void test_withValueHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object newValueHandler = new Object();
        CollectionType newCollectionType = originalCollectionType.withValueHandler(newValueHandler);

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertSame(elemT, newCollectionType.getContentType());
        assertSame(newValueHandler, newCollectionType.getValueHandler());
        assertSame(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStaticTyping());
    }

    @Test
    public void test_withContentValueHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object newValueHandler = new Object();
        CollectionType newCollectionType = originalCollectionType.withContentValueHandler(newValueHandler);

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertEquals(new SimpleType(String.class, newValueHandler, null), newCollectionType.getContentType());
        assertSame(valueHandler, newCollectionType.getValueHandler());
        assertSame(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStaticTyping());
    }

    @Test
    public void test_withStaticTyping() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = originalCollectionType.withStaticTyping();

        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertSame(superClass, newCollectionType.getSuperClass());
        assertSame(superInts, newCollectionType.getInterfaces());
        assertSame(elemT, newCollectionType.getContentType());
        assertSame(valueHandler, newCollectionType.getValueHandler());
        assertSame(typeHandler, newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStaticTyping());
    }

    @Test
    public void test_refine() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionType originalCollectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Class<?> refinedRawType = ArrayList.class;
        CollectionType refinedCollectionType = (CollectionType) originalCollectionType.refine(refinedRawType, bindings, superClass, superInts);

        assertEquals(refinedRawType, refinedCollectionType.getRawClass());
        assertEquals(bindings, refinedCollectionType.getBindings());
        assertSame(superClass, refinedCollectionType.getSuperClass());
        assertSame(superInts, refinedCollectionType.getInterfaces());
        assertSame(elemT, refinedCollectionType.getContentType());
        assertSame(valueHandler, refinedCollectionType.getValueHandler());
        assertSame(typeHandler, refinedCollectionType.getTypeHandler());
        assertEquals(asStatic, refinedCollectionType.isStaticTyping());
    }

    @Test
    public void testToString() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(rawType);
        JavaType[] superInts = {new SimpleType(rawType)};
        JavaType elemT = new SimpleType(String.class);

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals("[collection type; class " + rawType.getName() + ", contains " + elemT + "]", collectionType.toString());
    }

}