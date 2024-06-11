import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setup() {
        collectionLikeType = new CollectionLikeType(Map.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
    }

    @Test
    public void testConstruct() {
        assertEquals(Map.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(mapType, TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(Map.class, upgradedType.getRawClass());
        assertEquals(Integer.class, upgradedType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionLikeType modifiedType = collectionLikeType.withContentType(contentType);
        assertEquals(Map.class, modifiedType.getRawClass());
        assertEquals(Integer.class, modifiedType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new DummyTypeHandler();
        CollectionLikeType modifiedType = collectionLikeType.withTypeHandler(typeHandler);
        assertEquals(Map.class, modifiedType.getRawClass());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new DummyTypeHandler();
        CollectionLikeType modifiedType = collectionLikeType.withContentTypeHandler(contentTypeHandler);
        assertEquals(Map.class, modifiedType.getRawClass());
        assertEquals(contentTypeHandler, modifiedType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new DummyValueHandler();
        CollectionLikeType modifiedType = collectionLikeType.withValueHandler(valueHandler);
        assertEquals(Map.class, modifiedType.getRawClass());
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new DummyValueHandler();
        CollectionLikeType modifiedType = collectionLikeType.withContentValueHandler(contentValueHandler);
        assertEquals(Map.class, modifiedType.getRawClass());
        assertEquals(contentValueHandler, modifiedType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType modifiedType = collectionLikeType.withStaticTyping();
        assertEquals(Map.class, modifiedType.getRawClass());
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] superInterfaces = {};
        CollectionLikeType refinedType = collectionLikeType.refine(HashMap.class, new TypeBindings(), superClass, superInterfaces);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType().getRawClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType sameType = new CollectionLikeType(Map.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertTrue(collectionLikeType.equals(sameType));
        assertFalse(collectionLikeType.equals(null));
        assertFalse(collectionLikeType.equals("different type"));
    }

    @Test
    public void testToString() {
        String expected = "[collection-like type; class java.util.Map, contains class java.lang.String]";
        assertEquals(expected, collectionLikeType.toString());
    }

    /**
     * DummyTypeHandler class for testing purposes
     */
    private static class DummyTypeHandler {
    }

    /**
     * DummyValueHandler class for testing purposes
     */
    private static class DummyValueHandler {
    }
}