import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(Integer.class, type.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, SimpleType.construct(String.class));
        assertNotNull(upgradedType);
        assertEquals(ArrayList.class, upgradedType.getRawClass());
        assertEquals(String.class, upgradedType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withContentType(SimpleType.construct(String.class));
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(String.class, updatedType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withTypeHandler("testHandler");
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
        assertEquals("testHandler", updatedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withContentTypeHandler("testHandler");
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
        assertEquals("testHandler", updatedType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withValueHandler("testHandler");
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
        assertEquals("testHandler", updatedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withContentValueHandler("testHandler");
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
        assertEquals("testHandler", updatedType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType updatedType = baseType.withStaticTyping();
        assertNotNull(updatedType);
        assertEquals(ArrayList.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testUnderlyingClass() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals(ArrayList.class, type.getRawClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType type2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", type.toString());
    }
}