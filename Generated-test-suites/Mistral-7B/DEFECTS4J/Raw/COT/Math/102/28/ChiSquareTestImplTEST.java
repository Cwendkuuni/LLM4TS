import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        Assert.assertNotNull(chiSquareTestImpl);
    }

    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(distribution);
        Assert.assertEquals(distribution, chiSquareTestImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedOrObserved() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedOrObservedLengths() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithPositiveExpectedAndNonNegativeObserved() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        Assert.assertEquals(4.0, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidExpectedOrObserved() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidExpectedOrObservedLengths() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithInvalidExpectedOrObserved() throws Exception {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidAlphaGreaterThan05() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed, 0.6);
    }

    @Test
    public void testChiSquareTestWithValidExpectedObservedAndAlpha() throws Exception {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, alpha);
        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareComparisonWithValidObserved1Observed2() throws Exception {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 3};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertEquals(2.583, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonWithInvalidObserved1OrObserved2() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[1];
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonWithInvalidObserved1OrObserved2Lengths() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1};
        long[] observed2 = new long[0];
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareComparisonWithValidObserved1Observed2() throws Exception {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 3};
        double result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.042, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonWithInvalidAlphaGreaterThan05() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.6);
    }

    @Test
    public void testChiSquareComparisonWithValidObserved1Observed2AndAlpha() throws Exception {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 3};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckArray() {
        long[][] counts = new long[2][2];
        counts[0][0] = 1;
        counts[0][1] = 2;
        counts[1][0] = 3;
        counts[1][1] = 4;
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidArray() {
        long[][] counts = new long[1][2];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidNumberOfRows() {
        long[][] counts = new long[1][3];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidNumberOfColumns() {
        long[][] counts = new long[2][1];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidEntry() {
        long[][] counts = new long[2][2];
        counts[0][0] = -1;
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.checkArray(counts);
    }

}