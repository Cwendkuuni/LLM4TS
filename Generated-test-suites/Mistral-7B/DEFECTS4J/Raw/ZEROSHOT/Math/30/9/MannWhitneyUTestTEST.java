import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * JUnit 4 test file for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test for MannWhitneyUTest constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.FIXED, mannWhitneyUTest.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getTiesStrategy());
    }

    /**
     * Test for MannWhitneyUTest constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double nanrank(double nan) {
                // custom implementation for NaN ranking
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int tieValue(int currentRank, int numberOfTies) {
                // custom implementation for ties ranking
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNanStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getTiesStrategy());
    }

    /**
     * Test for mannWhitneyU method with sample sizes less than or equal to zero.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithZeroSampleSizes() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    /**
     * Test for mannWhitneyU method with null input.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullInput() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    /**
     * Test for mannWhitneyU method with equal samples.
     */
    @Test
    public void testMannWhitneyUWithEqualSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] y = {3.0, 3.0, 3.0, 3.0, 3.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(12.0, mannWhitneyU, 0.0);
    }

    /**
     * Test for mannWhitneyU method with different samples.
     */
    @Test
    public void testMannWhitneyUWithDifferentSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.0);
    }

    /**
     * Test for mannWhitneyUTest method with sample sizes less than or equal to zero.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithZeroSampleSizes() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test for mannWhitneyUTest method with null input.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullInput() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test for mannWhitneyUTest method with equal samples.
     */
    @Test
    public void testMannWhitneyUTestWithEqualSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] y = {3.0, 3.0, 3.0, 3.0, 3.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 1.0;
        assertEquals(expectedPValue, pValue, 0.0);
    }

    /**
     * Test for mannWhitneyUTest method with different samples.
     */
    @Test
    public void testMannWhitneyUTestWithDifferentSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.1111111111111111;
        assertEquals(expectedPValue, pValue, 0.0000000000000001);
    }
}