import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.util.NotFiniteNumberException;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 10.5;
        int expectedHash = new Double(value).hashCode();
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expectedHash = Arrays.hashCode(value);
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 7.0;
        double center = 3.0;
        double expectedNormalizedAngle = 4.716814692820413;
        double actualNormalizedAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.00001);
    }

    // Additional test cases can be added for the remaining methods

}