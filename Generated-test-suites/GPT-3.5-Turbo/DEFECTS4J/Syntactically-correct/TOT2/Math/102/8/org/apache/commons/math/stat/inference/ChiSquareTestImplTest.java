import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        assertEquals(0.5555555555555556, testImpl.chiSquare(expected, observed), 0.00000001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        assertEquals(0.682289137092043, testImpl.chiSquareTest(expected, observed), 0.00000001);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        assertFalse(testImpl.chiSquareTest(expected, observed, 0.01));
    }

    @Test
    public void testChiSquareWith2WayTable() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        assertEquals(0.06666666666666667, testImpl.chiSquare(counts), 0.00000001);
    }

    @Test
    public void testChiSquareTestWith2WayTable() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        assertEquals(0.9333333333333333, testImpl.chiSquareTest(counts), 0.00000001);
    }

    @Test
    public void testChiSquareTestWithAlphaAnd2WayTable() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        assertTrue(testImpl.chiSquareTest(counts, 0.01));
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertEquals(0.0, testImpl.chiSquareDataSetsComparison(observed1, observed2), 0.00000001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertEquals(0.5793969988588484, testImpl.chiSquareTestDataSetsComparison(observed1, observed2), 0.00000001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertFalse(testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.01));
    }

}