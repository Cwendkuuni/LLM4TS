import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        chiSquareTestImpl = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1L, 2L, 3L};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1L, 2L, 3L};
        double result = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1L, 2L, 3L};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTwoWay() {
        long[][] counts = {{1L, 2L}, {3L, 4L}};
        double result = chiSquareTestImpl.chiSquare(counts);
        assertEquals(1.7857, result, 0.0001);
    }

    @Test
    public void testChiSquareTestTwoWay() {
        long[][] counts = {{1L, 2L}, {3L, 4L}};
        double result = chiSquareTestImpl.chiSquareTest(counts);
        assertEquals(0.7173, result, 0.0001);
    }

    @Test
    public void testChiSquareTestTwoWayWithAlpha() {
        long[][] counts = {{1L, 2L}, {3L, 4L}};
        boolean result = chiSquareTestImpl.chiSquareTest(counts, 0.05);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1L, 2L, 3L};
        long[] observed2 = {3L, 2L, 1L};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.66667, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {1L, 2L, 3L};
        long[] observed2 = {3L, 2L, 1L};
        double result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.66667, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        long[] observed1 = {1L, 2L, 3L};
        long[] observed2 = {3L, 2L, 1L};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }
}