
import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, SimpleType.constructUnsafe(Float.class));
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(Float.class), collectionLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withContentType(SimpleType.constructUnsafe(Float.class));
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(Float.class), collectionLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withTypeHandler(new TypeHandler());
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
        assertNotNull(collectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withContentTypeHandler(new TypeHandler());
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
        assertNotNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withValueHandler(new ValueHandler());
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
        assertNotNull(collectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withContentValueHandler(new ValueHandler());
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
        assertNotNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = baseType.withStaticTyping();
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class).withStaticTyping(), collectionLikeType.getContentType());
        assertTrue(collectionLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) baseType.refine(ArrayList.class, TypeBindings.emptyBindings(),
                SimpleType.constructUnsafe(Integer.class), null);
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(SimpleType.constructUnsafe(Integer.class), collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.constructUnsafe(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(LinkedList.class, TypeBindings.emptyBindings(), SimpleType.constructUnsafe(Integer.class),
                null, SimpleType.constructUnsafe(String.class));
        assertTrue(collectionLikeType2.isTrueCollectionType());
    }

    private class TypeHandler {}

    private class ValueHandler {}
}
