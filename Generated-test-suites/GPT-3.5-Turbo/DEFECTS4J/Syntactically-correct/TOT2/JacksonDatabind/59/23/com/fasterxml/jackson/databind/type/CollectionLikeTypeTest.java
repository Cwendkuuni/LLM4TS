import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, JavaType.construct(String.class));
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(JavaType.construct(String.class), type.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, JavaType.construct(String.class));
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, JavaType.construct(Integer.class));
        assertNotNull(upgradedType);
        assertEquals(Integer.class, upgradedType.getContentType().getRawClass());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(String.class, null, null, null, JavaType.construct(String.class));
        CollectionLikeType type2 = CollectionLikeType.construct(String.class, null, null, null, JavaType.construct(String.class));
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, JavaType.construct(String.class));
        assertEquals("[collection-like type; class java.lang.String, contains class java.lang.String]", type.toString());
    }

    // Additional test cases can be added here

}