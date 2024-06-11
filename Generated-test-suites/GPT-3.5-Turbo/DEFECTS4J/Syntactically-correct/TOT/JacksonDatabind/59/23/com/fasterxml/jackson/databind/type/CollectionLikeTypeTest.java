
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setup() {
        collectionLikeType = new CollectionLikeType(String.class);
    }

    @Test
    public void testConstructor_withElementType_exists() {
        assertNotNull(collectionLikeType._elementType);
    }

    @Test
    public void testConstructor_withElementType_correctType() {
        assertEquals(String.class, collectionLikeType._elementType.getClass());
    }

    @Test
    public void testConstruct_ValidParameters_Success() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new SimpleType(String.class);
        CollectionLikeType result = CollectionLikeType.construct(rawType, bindings, superClass,
                superInts, elemT);

        assertEquals(rawType, result._class);
        assertEquals(bindings, result._bindings);
        assertEquals(superClass, result._superClass);
        assertEquals(superInts, result._superInterfaces);
        assertEquals(elemT, result._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withInvalidParameters_Exception() {
        Class<?> rawType = Collection.class;
        JavaType elemT = new SimpleType(String.class);
        CollectionLikeType.construct(rawType, elemT);
    }

    @Test
    public void testUpgradeFrom_withBaseType_Success() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(baseType.getClass(), result._class);
        assertEquals(baseType, result._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_withInvalidBaseType_Exception() {
        JavaType baseType = new SimpleType(String.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testNarrow_withSubclass_Success() {
        Class<?> subclass = String.class;
        JavaType result = collectionLikeType._narrow(subclass);

        assertEquals(subclass, result._class);
        assertEquals(collectionLikeType._bindings, result._bindings);
        assertEquals(collectionLikeType._superClass, result._superClass);
        assertEquals(collectionLikeType._superInterfaces, result._superInterfaces);
        assertEquals(collectionLikeType._elementType, result._elementType);
        assertEquals(collectionLikeType._valueHandler, result._valueHandler);
        assertEquals(collectionLikeType._typeHandler, result._typeHandler);
        assertEquals(collectionLikeType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentType_withSameContentType_ReturnsSelf() {
        JavaType contentType = collectionLikeType._elementType;
        JavaType result = collectionLikeType.withContentType(contentType);
        assertEquals(collectionLikeType, result);
    }

    @Test
    public void testWithContentType_withDifferentContentType_ReturnsNewInstance() {
        JavaType contentType = new SimpleType(Integer.class);
        JavaType result = collectionLikeType.withContentType(contentType);
        assertNotEquals(collectionLikeType, result);
        assertEquals(contentType, result._elementType);
    }

    @Test
    public void testWithTypeHandler_withValidParameter_Success() {
        Object handler = new Object();
        CollectionLikeType result = collectionLikeType.withTypeHandler(handler);

        assertEquals(collectionLikeType._class, result._class);
        assertEquals(collectionLikeType._bindings, result._bindings);
        assertEquals(collectionLikeType._superClass, result._superClass);
        assertEquals(collectionLikeType._superInterfaces, result._superInterfaces);
        assertEquals(collectionLikeType._elementType, result._elementType);
        assertEquals(collectionLikeType._valueHandler, result._valueHandler);
        assertEquals(handler, result._typeHandler);
        assertEquals(collectionLikeType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler_withValidParameter_Success() {
        Object handler = new Object();
        CollectionLikeType result = collectionLikeType.withContentTypeHandler(handler);

        assertEquals(collectionLikeType._class, result._class);
        assertEquals(collectionLikeType._bindings, result._bindings);
        assertEquals(collectionLikeType._superClass, result._superClass);
        assertEquals(collectionLikeType._superInterfaces, result._superInterfaces);
        assertNotEquals(collectionLikeType._elementType, result._elementType);
        assertEquals(handler, result._elementType._typeHandler);
        assertEquals(collectionLikeType._valueHandler, result._valueHandler);
        assertEquals(collectionLikeType._asStatic, result._asStatic);
    }

    @Test
    public void testWithValueHandler_withValidParameter_Success() {
        Object handler = new Object();
        CollectionLikeType result = collectionLikeType.withValueHandler(handler);

        assertEquals(collectionLikeType._class, result._class);
        assertEquals(collectionLikeType._bindings, result._bindings);
        assertEquals(collectionLikeType._superClass, result._superClass);
        assertEquals(collectionLikeType._superInterfaces, result._superInterfaces);
        assertEquals(collectionLikeType._elementType, result._elementType);
        assertEquals(handler, result._valueHandler);
        assertEquals(collectionLikeType._typeHandler, result._typeHandler);
        assertEquals(collectionLikeType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentValueHandler_withValidParameter_Success() {
        Object handler = new Object();
        CollectionLikeType result = collectionLikeType.withContentValueHandler(handler);

        assertEquals(collectionLikeType._class, result._class);
        assertEquals(collectionLikeType._bindings, result._bindings);
        assertEquals(collectionLikeType._superClass, result._superClass);
        assertEquals(collectionLikeType._superInterfaces, result._superInterfaces);
        assertNotEquals(collectionLikeType._elementType, result._elementType);
        assertEquals(handler, result._elementType._valueHandler);
        assertEquals(collectionLikeType._valueHandler, result._valueHandler);
        assertEquals(collectionLikeType._typeHandler, result._typeHandler);
        assertEquals(collectionLikeType._asStatic, result._asStatic);
    }

    @Test
    public void testWithStaticTyping_withAsStatic_ReturnsSelf() {
        collectionLikeType._asStatic = true;
        JavaType result = collectionLikeType.withStaticTyping();
        assertEquals(collectionLikeType, result);
    }

    @Test
    public void testWithStaticTyping_withNotAsStatic_ReturnsNewInstance() {
        collectionLikeType._asStatic = false;
        JavaType result = collectionLikeType.withStaticTyping();
        assertNotEquals(collectionLikeType, result);
        assertTrue(result._asStatic);
    }

    @Test
    public void testRefine_withValidParameters_Success() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new SimpleType(String.class);
        JavaType result = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, result._class);
        assertEquals(bindings, result._bindings);
        assertEquals(superClass, result._superClass);
        assertEquals(superInterfaces, result._superInterfaces);
        assertEquals(elementType, result._elementType);
    }

    @Test
    public void testEquals_withEqualObjects_ReturnsTrue() {
        CollectionLikeType other = new CollectionLikeType(String.class);
        assertTrue(collectionLikeType.equals(other));
    }

    @Test
    public void testEquals_withSameObjects_ReturnsTrue() {
        assertTrue(collectionLikeType.equals(collectionLikeType));
    }

    @Test
    public void testEquals_withDifferentClass_ReturnsFalse() {
        CollectionLikeType other = new CollectionLikeType(Integer.class);
        assertFalse(collectionLikeType.equals(other));
    }

    @Test
    public void testEquals_withDifferentElementType_ReturnsFalse() {
        CollectionLikeType other = new CollectionLikeType(Object.class);
        assertFalse(collectionLikeType.equals(other));
    }

    @Test
    public void testToString_ValidParameters_ReturnsStringRepresentation() {
        String expected = "[collection-like type; class java.lang.String, contains java.lang.String]";
        assertEquals(expected, collectionLikeType.toString());
    }
}

