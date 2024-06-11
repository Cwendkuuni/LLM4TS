import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstructWithElemT() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, TypeFactory.defaultInstance().constructType(String.class));
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        CollectionLikeType baseType = CollectionLikeType.construct(List.class, TypeFactory.defaultInstance().constructType(Integer.class));
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, TypeFactory.defaultInstance().constructType(String.class));
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        CollectionLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeFactory.defaultInstance().constructType(Integer.class));
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(List.class, TypeFactory.defaultInstance().constructType(Integer.class));
        CollectionLikeType type2 = CollectionLikeType.construct(List.class, TypeFactory.defaultInstance().constructType(Integer.class));
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", type.toString());
    }
}