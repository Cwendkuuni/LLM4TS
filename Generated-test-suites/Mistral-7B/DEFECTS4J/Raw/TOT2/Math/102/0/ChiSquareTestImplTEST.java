import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareMethodWithBasicInput() throws Exception {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0};
        double result = chiSquareTest.chiSquare(expected, observed);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareMethodWithEdgeCases() throws Exception {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 1.0};
        long[] observed = {1.0, 1.0};
        double result = chiSquareTest.chiSquare(expected, observed);
        double expectedResult = 0.0;
        assertEquals(expectedResult, result, 0.001);

        expected = new double[0];
        observed = new long[0];
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        expected = new double[]{-1.0, 1.0};
        observed = new long[]{1.0, 1.0};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        expected = new double[]{1.0, 1.0};
        observed = new long[]{1.0, 2.0};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        expected = new double[]{1.0, 1.0};
        observed = new long[]{1.0, 1.0};
        ChiSquareTestImpl chiSquareTestWithSmallData = new ChiSquareTestImpl();
        try {
            chiSquareTestWithSmallData.chiSquare(expected, observed);
            fail("MathException expected");
        } catch (MathException e) {
            // Pass
        }
    }
}