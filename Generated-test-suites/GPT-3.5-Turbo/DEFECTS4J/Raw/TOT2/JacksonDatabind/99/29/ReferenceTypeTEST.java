import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        ReferenceType refType = ReferenceType.construct(String.class, null, null, null, null);
        assertNotNull(refType);
        assertEquals(String.class, refType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);
        assertNotNull(refType);
        assertEquals(SimpleType.class, refType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullReferenceType() {
        JavaType baseType = SimpleType.construct(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    // More test cases for other public methods can be added here

}