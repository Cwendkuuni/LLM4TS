import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CollectionTypeTest {

    @Test
    public void testConstructWithRawTypeAndBindingsAndSuperclassAndSuperInterfacesAndElementType() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);
        assertNotNull(collectionType);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertNotNull(collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
    }

    @Test
    public void testConstructWithRawTypeAndElementType() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        assertNotNull(collectionType);
        assertEquals(listClass, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructWithRawTypeAndElementType() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, elemType);
    }

    @Test
    public void testNarrow() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, elemType);
        Class<?> sublistClass = ArrayListSubList.class;
        CollectionType subCollectionType = collectionType.narrow(sublistClass);
        assertNotNull(subCollectionType);
        assertEquals(sublistClass, subCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), subCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), subCollectionType.getSuperClass());
        assertNotNull(subCollectionType.getSuperInterfaces());
        assertEquals(elemType, subCollectionType.getElementType());
    }

    @Test
    public void testWithContentType() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        JavaType stringType = String.class;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withContentType(stringType);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(stringType, newCollectionType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withTypeHandler(new Object());
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(new Object());
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withValueHandler(new Object());
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withContentValueHandler(new Object());
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertNotNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = Integer.TYPE;
        CollectionType collectionType = new CollectionType(listClass, elemType);
        CollectionType newCollectionType = collectionType.refine(LinkedList.class, null, null, null);
        assertNotNull(newCollectionType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertNull(newCollectionType.getBindings());
        assertEquals(null, newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
    }
}