import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 3, 5};
        assertEquals(1.11, chiSquareTest.chiSquare(expected, observed), 0.01);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 3, 5};
        assertEquals(0.75, chiSquareTest.chiSquareTest(expected, observed), 0.01);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 3, 5};
        double alpha = 0.05;
        assertTrue(chiSquareTest.chiSquareTest(expected, observed, alpha));
    }

    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {{1, 2, 3}, {3, 2, 1}};
        assertEquals(1.50, chiSquareTest.chiSquare(counts), 0.01);
    }

    @Test
    public void testChiSquareTestWith2WayTable() throws MathException {
        long[][] counts = {{1, 2, 3}, {3, 2, 1}};
        assertEquals(0.78, chiSquareTest.chiSquareTest(counts), 0.01);
    }

    @Test
    public void testChiSquareTestWithAlphaAnd2WayTable() throws MathException {
        long[][] counts = {{1, 2, 3}, {3, 2, 1}};
        double alpha = 0.05;
        assertTrue(chiSquareTest.chiSquareTest(counts, alpha));
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        assertEquals(0.75, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        assertEquals(0.68, chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2), 0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double alpha = 0.05;
        assertTrue(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }

    @Test
    public void testCheckArrayWithInvalidInput() {
        long[][] counts = {{1, 2, 3}, {4, 5}};
        assertThrows(IllegalArgumentException.class, () -> {
            chiSquareTest.checkArray(counts);
        });
    }

}