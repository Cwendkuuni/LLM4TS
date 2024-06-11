import org.junit.Test;
import org.junit.Assert;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        double result = testImpl.chiSquare(expected, observed);
        Assert.assertEquals(1.6515151515151514, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        try {
            double result = testImpl.chiSquareTest(expected, observed);
            Assert.assertEquals(0.6417520837714277, result, 0.0001);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {4, 5, 6};
        try {
            boolean result = testImpl.chiSquareTest(expected, observed, 0.2);
            Assert.assertTrue(result);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareWithArray() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double result = testImpl.chiSquare(counts);
        Assert.assertEquals(0.75, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithArray() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        try {
            double result = testImpl.chiSquareTest(counts);
            Assert.assertEquals(0.89495, result, 0.0001);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithArrayAndAlpha() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        try {
            boolean result = testImpl.chiSquareTest(counts, 0.1);
            Assert.assertTrue(result);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        double result = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertEquals(1.5833333333333333, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            double result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
            Assert.assertEquals(0.6265951142081781, result, 0.0001);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        try {
            boolean result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
            Assert.assertTrue(result);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

}