import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 2};
        assertEquals(0.5, chiSquareTestImpl.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 2};
        assertEquals(0.7165, chiSquareTestImpl.chiSquareTest(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTestSignificanceLevel() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 2};
        double alpha = 0.05;
        assertFalse(chiSquareTestImpl.chiSquareTest(expected, observed, alpha));
    }

    @Test
    public void testChiSquare2WayTable() {
        long[][] counts = {{1, 2}, {2, 4}};
        assertEquals(0.25, chiSquareTestImpl.chiSquare(counts), 0.0001);
    }

    @Test
    public void testChiSquareTest2WayTable() throws MathException {
        long[][] counts = {{1, 2}, {2, 4}};
        assertEquals(0.8821, chiSquareTestImpl.chiSquareTest(counts), 0.0001);
    }

    @Test
    public void testChiSquareTestSignificanceLevel2WayTable() throws MathException {
        long[][] counts = {{1, 2}, {2, 4}};
        double alpha = 0.05;
        assertFalse(chiSquareTestImpl.chiSquareTest(counts, alpha));
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertEquals(0.1481, chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertEquals(0.1469, chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonSignificanceLevel() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double alpha = 0.05;
        assertFalse(chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }
}