import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refdType = new SimpleType(Integer.class);
        ReferenceType upgradedType = ReferenceType.upgradeFrom(baseType, refdType);
        
        assertNotNull(upgradedType);
        assertEquals(baseType, upgradedType.getContentType());
        assertEquals(upgradedType, upgradedType.getAnchorType());
    }

    @Test
    public void testConstruct() {
        JavaType refdType = new SimpleType(Double.class);
        ReferenceType constructedType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refdType);
        
        assertNotNull(constructedType);
        assertEquals(refdType, constructedType.getContentType());
        assertEquals(constructedType, constructedType.getAnchorType());
    }

    @Test
    public void testConstructor() {
        JavaType refdType = new SimpleType(Long.class);
        ReferenceType refType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        
        assertNotNull(refType);
        assertEquals(refdType, refType.getContentType());
        assertEquals(refType, refType.getAnchorType());
    }

    // Additional test cases for other methods in ReferenceType class

}