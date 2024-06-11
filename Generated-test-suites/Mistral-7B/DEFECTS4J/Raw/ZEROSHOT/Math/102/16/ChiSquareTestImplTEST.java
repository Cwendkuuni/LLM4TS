import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareDistributionImplTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = chiSquareTestImpl.getDistribution();
        assertEquals("Incorrect distribution instance", distribution, chiSquareTestImpl.getDistribution());
    }

    @Test
    public void testChiSquareConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        ChiSquaredDistribution distribution = chiSquareTestImpl.getDistribution();
        assertEquals("Incorrect distribution instance", distribution, chiSquareTestImpl.getDistribution());
    }

    @Test
    public void testChiSquare_simple() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3};
        double chiSquareResult = chiSquareTestImpl.chiSquare(expected, observed);
        double[] df = new double[]{expected.length - 1, expected.length - 1};
        ChiSquareDistribution chiSquareDist = new ChiSquareDistributionImpl(df[0]);
        double pValue = chiSquareDist.cumulativeProbability(chiSquareResult);
        double expectedPValue = 0.05;
        assertEquals("Incorrect p-value for simple test", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquare_arrays() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double chiSquareResult = chiSquareTestImpl.chiSquare(counts);
        ChiSquareDistribution chiSquareDist = new ChiSquareDistributionImpl(5.0);
        double pValue = chiSquareDist.cumulativeProbability(chiSquareResult);
        double expectedPValue = 0.01;
        assertEquals("Incorrect p-value for arrays test", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquare_datasetsComparison_simple() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 4};
        double chiSquareResult = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        ChiSquareDistribution chiSquareDist = new ChiSquareDistributionImpl(2.0);
        double pValue = chiSquareDist.cumulativeProbability(chiSquareResult);
        double expectedPValue = 0.1;
        assertEquals("Incorrect p-value for datasets comparison simple test", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquare_datasetsComparison_arrays() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[][]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 4};
        long[][] counts = new long[][]{{1, 1, 1}, {1, 2, 2}, {1, 1, 2}};
        double chiSquareResult = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        ChiSquareDistribution chiSquareDist = new ChiSquareDistributionImpl(3.0);
        double pValue = chiSquareDist.cumulativeProbability(chiSquareResult);
        double expectedPValue = 0.05;
        assertEquals("Incorrect p-value for datasets comparison arrays test", expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_invalidData_simple() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3, 2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_invalidData_arrays() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 2, 4}, {3, 4, 5}, {3, 4, 4}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_invalidData_datasetsComparison_simple() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 2, 4};
        long[] observed3 = new long[]{3, 3, 4};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, observed3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_invalidData_datasetsComparison_arrays() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts1 = new long[][]{{1, 2, 3}, {2, 2, 4}, {3, 4, 5}};
        long[][] counts2 = new long[][]{{1, 2, 3}, {2, 2, 4}, {3, 4, 4}};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(counts1, counts2);
    }
}