import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstructor() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, JavaType.String, JavaType.String, null, null, false);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getErasedClass());
        assertEquals(JavaType.String, referenceType.getAnchorType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.String;
        JavaType referenceType = JavaType.Integer;
        ReferenceType upgradedType = ReferenceType.upgradeFrom(baseType, referenceType);
        assertNotNull(upgradedType);
        assertEquals(JavaType.String, upgradedType.withContentType(JavaType.String));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefType() {
        JavaType baseType = JavaType.String;
        JavaType referenceType = null;
        ReferenceType.upgradeFrom(baseType, referenceType);
    }
    
    // Add more test cases for other public methods in the ReferenceType class
    
}