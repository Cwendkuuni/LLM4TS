import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.lang.Class;
import java.lang.reflect.Type;

public class CollectionTypeTest {

    @Test
    public void testConstructorWithArguments() {
        Class<?> collT = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(List.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = CollectionType.construct(collT, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
        assertEquals(collT, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperclass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertEquals(false, collectionType.isStatic());
    }

    @Test
    public void testConstructorWithBaseAndElementType() {
        Type base = TypeToken.of(ArrayList.class).getType();
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType((TypeBase) base, elemT);
        assertNotNull(collectionType);
        assertEquals(base.getTypeName().substring(0, base.getTypeName().lastIndexOf('.')), collectionType.getRawClass().getName());
        assertEquals(null, collectionType.getTypeBindings());
        assertNull(collectionType.getSuperclass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertEquals(false, collectionType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructWithRawTypeAndElementTypeThrowsException() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        Class<?> rawType = ArrayList.class;
        Class<?> subclass = LinkedList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null,
                _bogusSuperClass(rawType), new JavaType[0], JavaType.construct(Object.class));
        CollectionType narrowedCollectionType = collectionType.narrow(subclass);
        assertNotNull(narrowedCollectionType);
        assertEquals(subclass, narrowedCollectionType.getRawClass());
        assertEquals(collectionType.getTypeBindings(), narrowedCollectionType.getTypeBindings());
        assertEquals(collectionType.getSuperclass(), narrowedCollectionType.getSuperclass());
        assertArrayEquals(collectionType.getSuperInterfaces(), narrowedCollectionType.getSuperInterfaces());
        assertEquals(collectionType.getContentType(), narrowedCollectionType.getContentType());
        assertEquals(false, narrowedCollectionType.isStatic());
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        JavaType originalContentType = JavaType.construct(Integer.class);
        JavaType newContentType = JavaType.construct(Double.class);
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], originalContentType);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertNotEquals(originalContentType, newCollectionType.getContentType());
        assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        Object valueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], JavaType.construct(Object.class));
        CollectionType newCollectionType = collectionType.withTypeHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertNotSame(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
        assertEquals(valueHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        Object valueHandler = new Object();
        JavaType originalContentType = JavaType.construct(Integer.class);
        JavaType newContentType = JavaType.construct(Double.class);
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], originalContentType);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertNotEquals(originalContentType, newCollectionType.getContentType());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNotSame(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        Object valueHandler = new Object();
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], JavaType.construct(Object.class));
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertNotSame(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        Object valueHandler = new Object();
        JavaType originalContentType = JavaType.construct(Integer.class);
        JavaType newContentType = JavaType.construct(Double.class);
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], originalContentType);
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertNotEquals(originalContentType, newCollectionType.getContentType());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNotSame(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> collectionTypeRawClass = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(collectionTypeRawClass, null,
                _bogusSuperClass(collectionTypeRawClass), new JavaType[0], JavaType.construct(Object.class));
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertTrue(newCollectionType.isStatic());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(List.class);
        JavaType[] superInterfaces = new JavaType[]{JavaType.construct(Cloneable.class)};
        JavaType elemT = JavaType.construct(Object.class);
        CollectionType collectionType = CollectionType.construct(rawType, bindings,
                superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.refine(LinkedList.class, bindings, null, null);
        assertNotNull(newCollectionType);
        assertEquals(LinkedList.class, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getTypeBindings());
        assertNull(newCollectionType.getSuperclass());
        assertArrayEquals(superInterfaces, newCollectionType.getSuperInterfaces());
        assertEquals(elemT, newCollectionType.getContentType());
        assertEquals(false, newCollectionType.isStatic());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        // Placeholder for a bogus superclass, you should replace this with the actual superclass of CollectionType
        return Object.class;
    }

}