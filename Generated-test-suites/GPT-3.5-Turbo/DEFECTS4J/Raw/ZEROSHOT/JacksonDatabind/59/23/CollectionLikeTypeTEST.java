import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionLikeType);
        assertEquals(rawType, collectionLikeType.getRawClass());
        assertEquals(bindings, collectionLikeType.getBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInts, collectionLikeType.getInterfaces());
        assertEquals(elemT, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(Collection.class);
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(collectionLikeType);
        assertEquals(baseType, collectionLikeType.getBaseType());
        assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(subclass, bindings, superClass, superInterfaces, elementType, null, null, false);
        JavaType narrowedType = collectionLikeType._narrow(subclass);
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInterfaces, narrowedType.getInterfaces());
        assertEquals(elementType, narrowedType.getElementType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType contentType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, contentType, null, null, false);
        JavaType withContentType = collectionLikeType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(rawClass, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertEquals(superInterfaces, withContentType.getInterfaces());
        assertEquals(contentType, withContentType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        Object typeHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType withTypeHandler = collectionLikeType.withTypeHandler(typeHandler);
        assertNotNull(withTypeHandler);
        assertEquals(rawClass, withTypeHandler.getRawClass());
        assertEquals(bindings, withTypeHandler.getBindings());
        assertEquals(superClass, withTypeHandler.getSuperClass());
        assertEquals(superInterfaces, withTypeHandler.getInterfaces());
        assertEquals(elementType, withTypeHandler.getElementType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        Object typeHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType withContentTypeHandler = collectionLikeType.withContentTypeHandler(typeHandler);
        assertNotNull(withContentTypeHandler);
        assertEquals(rawClass, withContentTypeHandler.getRawClass());
        assertEquals(bindings, withContentTypeHandler.getBindings());
        assertEquals(superClass, withContentTypeHandler.getSuperClass());
        assertEquals(superInterfaces, withContentTypeHandler.getInterfaces());
        assertEquals(elementType.withTypeHandler(typeHandler), withContentTypeHandler.getElementType());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        Object valueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType withValueHandler = collectionLikeType.withValueHandler(valueHandler);
        assertNotNull(withValueHandler);
        assertEquals(rawClass, withValueHandler.getRawClass());
        assertEquals(bindings, withValueHandler.getBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertEquals(superInterfaces, withValueHandler.getInterfaces());
        assertEquals(elementType, withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        Object valueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType withContentValueHandler = collectionLikeType.withContentValueHandler(valueHandler);
        assertNotNull(withContentValueHandler);
        assertEquals(rawClass, withContentValueHandler.getRawClass());
        assertEquals(bindings, withContentValueHandler.getBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertEquals(superInterfaces, withContentValueHandler.getInterfaces());
        assertEquals(elementType.withValueHandler(valueHandler), withContentValueHandler.getElementType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawClass = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawClass, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType withStaticTyping = collectionLikeType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(rawClass, withStaticTyping.getRawClass());
        assertEquals(bindings, withStaticTyping.getBindings());
        assertEquals(superClass, withStaticTyping.getSuperClass());
        assertEquals(superInterfaces, withStaticTyping.getInterfaces());
        assertEquals(elementType.withStaticTyping(), withStaticTyping.getElementType());
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elementType, null, null, false);
        JavaType refinedType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(elementType, refinedType.getElementType());
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elementType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elementType, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elementType, null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elementType, null, null, false);
        String expected = "[collection-like type; class java.util.Collection, contains "+elementType+"]";
        assertEquals(expected, collectionLikeType.toString());
    }

}