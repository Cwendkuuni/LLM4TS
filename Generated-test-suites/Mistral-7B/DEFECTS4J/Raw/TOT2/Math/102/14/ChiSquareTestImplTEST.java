import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testCreateInstance() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        Assert.assertNotNull(testInstance);
    }

    @Test
    public void testChiSquareMethodWithValidInputs() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 4};

        double result = testInstance.chiSquare(expected, observed);
        double expectedResult = 3.0;

        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidInputsSizeMismatch() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3, 4};

        testInstance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidInputsNegativeExpected() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, -2.0};
        long[] observed = new long[]{2, 3, 4};

        testInstance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidInputsNegativeObserved() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{-2, 3, 4};

        testInstance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidInputsMismatchedSizes() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3};

        testInstance.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithValidInputs() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 4};

        double pValue = testInstance.chiSquareTest(expected, observed);
        double expectedPValue = 0.95;

        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidInputsSizeMismatch() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3, 4};

        testInstance.chiSquareTest(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestMethodWithInvalidInputsInvalidDegreesOfFreedom() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3, 4};

        testInstance.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithValidInputsAndAlpha() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 4};
        double alpha = 0.05;

        boolean result = testInstance.chiSquareTest(expected, observed, alpha);
        boolean expectedResult = false;

        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareTestMethodWithValidInputsAndAlphaPassed() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 4};
        double alpha = 0.95;

        boolean result = testInstance.chiSquareTest(expected, observed, alpha);
        boolean expectedResult = true;

        Assert.assertTrue(result);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethodWithValidInputs() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 4};
        long[] observed2 = new long[]{3, 2, 1};

        double result = testInstance.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 3.0;

        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidInputsSizeMismatchObserved1() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2};
        long[] observed2 = new long[]{3, 2, 1};

        testInstance.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidInputsSizeMismatchObserved2() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 4};
        long[] observed2 = new long[]{3};

        testInstance.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputs() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 4};
        long[] observed2 = new long[]{3, 2, 1};
        double alpha = 0.05;

        boolean result = testInstance.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        boolean expectedResult = false;

        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputsAndAlphaPassed() {
        ChiSquareTestImpl testInstance = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 4};
        long[] observed2 = new long[]{3, 2, 1};
        double alpha = 0.95;

        boolean result = testInstance.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        boolean expectedResult = true;

        Assert.assertTrue(result);
    }
}