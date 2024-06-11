import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionLikeType(Collection.class, new TypeBindings(), null, null, new JavaType());
    }

    @Test
    public void testConstruction() {
        assertNotNull(collectionType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(0, collectionType.getBindings().size());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertNotNull(collectionType.getElementType());
    }

    @Test
    public void testConstruct() {
        CollectionLikeType constructedType = CollectionLikeType.construct(Collection.class, new TypeBindings(), null, null, new JavaType());
        assertNotNull(constructedType);
        assertEquals(Collection.class, constructedType.getRawClass());
        assertEquals(0, constructedType.getBindings().size());
        assertNull(constructedType.getSuperClass());
        assertNull(constructedType.getSuperInterfaces());
        assertNull(constructedType.getValueHandler());
        assertNull(constructedType.getTypeHandler());
        assertFalse(constructedType.isStatic());
        assertNotNull(constructedType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(upgradedType);
        assertTrue(upgradedType instanceof CollectionLikeType);
        assertEquals(baseType, upgradedType);
        assertEquals(elementType, upgradedType.getElementType());
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionType.narrow(Collection.class);
        assertNotNull(narrowedType);
        assertTrue(narrowedType instanceof CollectionLikeType);
        assertEquals(Collection.class, narrowedType.getRawClass());
        assertEquals(collectionType.getBindings(), narrowedType.getBindings());
        assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), narrowedType.getElementType());
        assertEquals(collectionType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(collectionType.isStatic(), narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType withContentType = collectionType.withContentType(contentType);
        assertNotSame(collectionType, withContentType);
        assertTrue(withContentType instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withContentType.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentType.getSuperInterfaces());
        assertEquals(contentType, withContentType.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentType.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionLikeType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotSame(collectionType, withTypeHandler);
        assertTrue(withTypeHandler instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withTypeHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withTypeHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withTypeHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), withTypeHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withTypeHandler.getValueHandler());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        CollectionLikeType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(collectionType, withContentTypeHandler);
        assertTrue(withContentTypeHandler instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withContentTypeHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentTypeHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentTypeHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(contentTypeHandler), withContentTypeHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentTypeHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentTypeHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionLikeType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotSame(collectionType, withValueHandler);
        assertTrue(withValueHandler instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withValueHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withValueHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withValueHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withValueHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        CollectionLikeType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertNotSame(collectionType, withContentValueHandler);
        assertTrue(withContentValueHandler instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withContentValueHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentValueHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentValueHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(contentValueHandler), withContentValueHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentValueHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentValueHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType withStaticTyping = collectionType.withStaticTyping();
        assertNotSame(collectionType, withStaticTyping);
        assertTrue(withStaticTyping instanceof CollectionLikeType);
        assertEquals(collectionType.getBindings(), withStaticTyping.getBindings());
        assertEquals(collectionType.getSuperClass(), withStaticTyping.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withStaticTyping.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), withStaticTyping.getElementType());
        assertEquals(collectionType.getValueHandler(), withStaticTyping.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {new JavaType(), new JavaType()};
        CollectionLikeType refinedType = collectionType.refine(Collection.class, new TypeBindings(), superClass, superInterfaces);
        assertNotSame(collectionType, refinedType);
        assertTrue(refinedType instanceof CollectionLikeType);
        assertEquals(Collection.class, refinedType.getRawClass());
        assertEquals(collectionType.getBindings(), refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), refinedType.getElementType());
        assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(collectionType.isStatic(), refinedType.isStatic());
    }

    @Test
    public void testIsContainerType() {
        assertTrue(collectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = collectionType.getContentType();
        assertNotNull(elementType);
        assertEquals(collectionType.getElementType(), elementType);
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = collectionType.getContentValueHandler();
        assertNull(valueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = collectionType.getContentTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testHasHandlers() {
        assertFalse(collectionType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        collectionType.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        collectionType.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        assertEquals("java.util.Collection<com.fasterxml.jackson.databind.type.JavaType>;",
                collectionType.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        assertTrue(collectionType.equals(collectionType));
        assertFalse(collectionType.equals(null));
        assertFalse(collectionType.equals("string"));

        CollectionLikeType sameType = new CollectionLikeType(Collection.class, new TypeBindings(), null, null, new JavaType());
        assertTrue(collectionType.equals(sameType));
        assertTrue(sameType.equals(collectionType));

        CollectionLikeType differentType = new CollectionLikeType(Collection.class, new TypeBindings(), null, null, new JavaType());
        differentType.withContentType(new JavaType());
        assertFalse(collectionType.equals(differentType));
        assertFalse(differentType.equals(collectionType));
    }

    @Test
    public void testToString() {
        String expected = "[collection-like type; class java.util.Collection, contains com.fasterxml.jackson.databind.type.JavaType]";
        assertEquals(expected, collectionType.toString());
    }
}