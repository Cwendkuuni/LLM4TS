import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquareTestImpl chiSquareTestImplWithDistribution = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    // Test cases for the methods chiSquare, chiSquareTest, chiSquareTest, chiSquare, chiSquareTest, chiSquareTest

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[0];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    // Test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithMismatchedArrays() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[3];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpectedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1, 1};
        long[] observed = new long[]{1, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeObservedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1, 1};
        long[] observed = new long[]{-1, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[0];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = MathRuntimeException.class)
    public void testChiSquareTestWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[2];
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(-1.0);
        chiSquareTestImpl.setDistribution(chiSquaredDistribution);
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = MathRuntimeException.class)
    public void testChiSquareTestDataSetsWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1};
        long[] observed2 = new long[]{1, 1};
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(-1.0);
        chiSquareTestImpl.setDistribution(chiSquaredDistribution);
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = null;
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsWithEqualInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1};
        long[] observed2 = new long[]{1, 1};
        double chiSquareTestResult = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, chiSquareTestResult, 1e-14);
    }

    @Test
    public void testChiSquareDataSetsWithDifferentInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{2, 1};
        double chiSquareTestResult = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquareTestResult = 2.0;
        assertEquals(expectedChiSquareTestResult, chiSquareTestResult, 1e-14);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithEmptyObservedArrays() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        try {
            chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    // Test cases for the protected methods

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDistributionFactory() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        DistributionFactory distributionFactory = chiSquareTestImpl.getDistributionFactory();
    }
}