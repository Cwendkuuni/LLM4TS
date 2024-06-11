import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        chiSquareTestImpl = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1, 2, 3};
        long[] observed = {2, 3, 4};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(0.25, result, 0.001);
    }

    @Test
    public void testChiSquareTest() {
        double[] expected = {1, 2, 3};
        long[] observed = {2, 3, 4};
        double result = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(0.324, result, 0.001);
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        double[] expected = {1, 2, 3};
        long[] observed = {2, 3, 4};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, alpha);
        assertTrue(result);
    }

    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};
        double result = chiSquareTestImpl.chiSquare(counts);
        assertEquals(1.333, result, 0.001);
    }

    @Test
    public void testChiSquareTestWith2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};
        double result = chiSquareTestImpl.chiSquareTest(counts);
        assertEquals(0.15, result, 0.001);
    }

    @Test
    public void testChiSquareTestWithAlphaAnd2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTest(counts, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(5.556, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        double result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.21, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {4, 5, 6};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

}