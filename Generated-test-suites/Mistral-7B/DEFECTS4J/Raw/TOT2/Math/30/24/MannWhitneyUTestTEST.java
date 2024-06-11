import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.stat.inference.MannWhitneyUTest.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        // Test with two empty arrays
        double[] x = new double[0];
        double[] y = new double[0];
        try {
            mannWhitneyU(x, y);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with two arrays of same length and same elements
        double[] x1 = new double[]{1.0, 2.0, 3.0};
        double[] y1 = new double[]{1.0, 2.0, 3.0};
        double result = mannWhitneyU(x1, y1);
        Assert.assertEquals(6.0, result, 0.001);

        // Test with two arrays of same length but different elements
        double[] x2 = new double[]{1.0, 2.0, 3.0};
        double[] y2 = new double[]{4.0, 5.0, 6.0};
        result = mannWhitneyU(x2, y2);
        Assert.assertEquals(10.0, result, 0.001);

        // Test with two arrays of different lengths
        double[] x3 = new double[]{1.0, 2.0, 3.0};
        double[] y3 = new double[]{4.0, 5.0};
        result = mannWhitneyU(x3, y3);
        Assert.assertEquals(7.0, result, 0.001);

        // Test with arrays containing NaN values
        double[] x4 = new double[]{Double.NaN, 2.0, 3.0};
        double[] y4 = new double[]{1.0, Double.NaN, 3.0};
        try {
            mannWhitneyU(x4, y4);
            Assert.fail("Expected ConvergenceException or NullArgumentException");
        } catch (ConvergenceException | NullArgumentException e) {
            // Expected
        }

        // Test with arrays containing duplicate values
        double[] x5 = new double[]{1.0, 2.0, 2.0, 3.0};
        double[] y5 = new double[]{1.0, 2.0, 3.0};
        try {
            mannWhitneyU(x5, y5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test mannWhitneyUTest method
        double[] x6 = new double[]{1.0, 2.0, 3.0};
        double[] y6 = new double[]{4.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest(x6, y6);
        Assert.assertEquals(0.05, pValue, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[0], new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.calculateAsymptoticPValue(0.0, 1, 1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.calculateAsymptoticPValue(Double.MAX_VALUE, 1, 1);
    }
}