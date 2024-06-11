import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refdType = new SimpleType(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);
        
        assertNotNull(refType);
        assertEquals(baseType, refType.getContentType());
    }

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(Double.class);
        ReferenceType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        
        assertNotNull(refType);
        assertEquals(Integer.class, refType.getRawClass());
    }

    // Add more test cases for other methods as per requirement

}