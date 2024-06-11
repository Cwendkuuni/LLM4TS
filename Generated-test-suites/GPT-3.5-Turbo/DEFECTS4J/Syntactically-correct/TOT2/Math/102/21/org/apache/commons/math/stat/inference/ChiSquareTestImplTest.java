import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {3, 2, 1};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {3, 2, 1};
        double result = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.1353, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {3, 2, 1};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareByCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{3, 2, 1}, {1, 3, 2}};
        double result = chiSquareTest.chiSquare(counts);
        assertEquals(4.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTestByCounts() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{3, 2, 1}, {1, 3, 2}};
        double result = chiSquareTest.chiSquareTest(counts);
        assertEquals(0.1711, result, 0.0001);
    }

    @Test
    public void testChiSquareTestByCountsWithAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{3, 2, 1}, {1, 3, 2}};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTest(counts, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.8333, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.5993, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double alpha = 0.05;
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }
}