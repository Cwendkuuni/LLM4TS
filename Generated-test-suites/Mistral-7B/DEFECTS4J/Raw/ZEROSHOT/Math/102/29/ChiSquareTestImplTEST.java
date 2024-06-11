import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTestImpl.setDistribution(distribution);

        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        double chiSquare = chiSquareTestImpl.chiSquare(counts);
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        boolean nullHypothesisRejected = chiSquareTestImpl.chiSquareTest(counts, 0.05);

        assertEquals(10.5, chiSquare, 0.01);
        assertEquals(0.025, pValue, 0.001);
        assertTrue(nullHypothesisRejected);

        long[] observed1 = new long[]{1, 2, 3, 4, 5};
        long[] observed2 = new long[]{2, 3, 3, 3, 2};
        double chiSquareDataSetComparison = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double pValueDataSetComparison = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        boolean nullHypothesisRejectedDataSetComparison = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);

        assertEquals(7.81475823656, chiSquareDataSetComparison, 0.01);
        assertEquals(0.03774607425, pValueDataSetComparison, 0.001);
        assertTrue(nullHypothesisRejectedDataSetComparison);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput1() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{2, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput2() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = new long[]{2, 3};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput3() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput4() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 1, 0}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput5() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 1}, {3, 1, 0, 2}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput6() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4, 5};
        long[] observed2 = new long[]{2, 3, 3, 3, 2};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, -0.05);
    }
}