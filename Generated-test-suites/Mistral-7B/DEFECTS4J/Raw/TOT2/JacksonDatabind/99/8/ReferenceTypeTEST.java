import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(int[].class);
        JavaType refdType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        JavaType expectedResult = new ReferenceType(baseType.getRawClass(), refdType);
        assertEquals(expectedResult, referenceType);
    }

    // Add tests for edge cases and exceptions here
}