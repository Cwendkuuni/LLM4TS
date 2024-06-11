import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        // Test upgrading from base type to ReferenceType with valid referenced type
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        
        assertNotNull(referenceType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFromMissingRefType() {
        // Test upgrading from base type to ReferenceType with missing referenced type
        JavaType baseType = new SimpleType(String.class);
        
        try {
            ReferenceType.upgradeFrom(baseType, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods
    
}