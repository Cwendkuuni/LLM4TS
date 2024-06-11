import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNotNull(collectionLikeType);
        assertEquals(String.class, collectionLikeType.getRawClass());
        assertNull(collectionLikeType.getBindings());
        assertNull(collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertNull(collectionLikeType.getElementType());
        assertNull(collectionLikeType.getValueHandler());
        assertNull(collectionLikeType.getTypeHandler());
        assertFalse(collectionLikeType.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(String.class, null, null, null, null);
        JavaType elementType = SimpleType.construct(Integer.class, null, null, null, null);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(collectionLikeType);
        assertEquals(SimpleType.class, collectionLikeType.getClass());
        assertEquals(baseType, collectionLikeType.getBaseType());
        assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test
    public void testNarrow() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        JavaType narrowedType = collectionLikeType._narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        JavaType contentType = SimpleType.construct(Integer.class, null, null, null, null);
        CollectionLikeType modifiedType = (CollectionLikeType) collectionLikeType.withContentType(contentType);
        assertNotNull(modifiedType);
        assertEquals(contentType, modifiedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionLikeType modifiedType = collectionLikeType.withTypeHandler(typeHandler);
        assertNotNull(modifiedType);
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionLikeType modifiedType = collectionLikeType.withContentTypeHandler(typeHandler);
        assertNotNull(modifiedType);
        assertEquals(typeHandler, modifiedType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object valueHandler = new Object();
        CollectionLikeType modifiedType = collectionLikeType.withValueHandler(valueHandler);
        assertNotNull(modifiedType);
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object valueHandler = new Object();
        CollectionLikeType modifiedType = collectionLikeType.withContentValueHandler(valueHandler);
        assertNotNull(modifiedType);
        assertEquals(valueHandler, modifiedType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType modifiedType = collectionLikeType.withStaticTyping();
        assertNotNull(modifiedType);
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testRefine() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        JavaType refinedType = collectionLikeType.refine(Object.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(Object.class, refinedType.getRawClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNull(collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertFalse(collectionLikeType.hasHandlers());
    }

}