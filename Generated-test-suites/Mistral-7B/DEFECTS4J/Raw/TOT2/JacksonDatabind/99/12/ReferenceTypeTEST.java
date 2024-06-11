import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refdType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        JavaType expectedReferenceType = new ReferenceType(baseType.getClass(), refdType);
        assertEquals(expectedReferenceType, referenceType);
    }
}