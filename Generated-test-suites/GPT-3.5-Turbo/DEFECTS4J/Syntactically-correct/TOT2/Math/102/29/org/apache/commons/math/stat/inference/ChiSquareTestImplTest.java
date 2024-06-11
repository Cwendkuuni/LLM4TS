import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // typical case
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        long[] observed = {1, 2, 2, 4};
        assertEquals(0.75, test.chiSquare(expected, observed), 0.001);

        // edge case: empty arrays
        expected = new double[0];
        observed = new long[0];
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // typical case
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        long[] observed = {1, 2, 2, 4};
        assertEquals(0.454, test.chiSquareTest(expected, observed), 0.001);

        // edge case: alpha out of bounds
        try {
            test.chiSquareTest(expected, observed, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    // Additional test methods for other public methods can be added here

}