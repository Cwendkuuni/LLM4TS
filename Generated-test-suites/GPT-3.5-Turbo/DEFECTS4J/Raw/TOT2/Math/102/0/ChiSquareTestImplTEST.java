import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        double result = chiSquareTest.chiSquare(expected, observed);
        Assert.assertEquals(0.6666666666666666, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        double result = chiSquareTest.chiSquareTest(expected, observed);
        Assert.assertEquals(0.6666666666666666, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);
        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareWith2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double result = chiSquareTest.chiSquare(counts);
        Assert.assertEquals(0.4375, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWith2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double result = chiSquareTest.chiSquareTest(counts);
        Assert.assertEquals(0.5083199243082477, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithAlphaAnd2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTest(counts, alpha);
        Assert.assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.5833333333333334, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.4614396997220738, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, -3}};
        chiSquareTest.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3};
        double alpha = 0.6;
        chiSquareTest.chiSquareTest(expected, observed, alpha);
    }
}