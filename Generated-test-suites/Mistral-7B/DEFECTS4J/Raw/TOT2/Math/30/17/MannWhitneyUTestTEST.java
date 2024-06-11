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
            Assert.fail("Expected NoDataException but no exception was thrown.");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with two arrays of the same length and same values
        double[] x1 = new double[]{1.0, 2.0, 3.0};
        double[] y1 = new double[]{1.0, 2.0, 3.0};

        Assert.assertEquals(6.0, mannWhitneyU(x1, y1), 0.001);

        // Test with two arrays of the same length but different values
        double[] x2 = new double[]{1.0, 2.0, 3.0};
        double[] y2 = new double[]{3.0, 2.0, 1.0};

        Assert.assertEquals(3.0, mannWhitneyU(x2, y2), 0.001);

        // Test with two arrays of different lengths
        double[] x3 = new double[]{1.0, 2.0, 3.0};
        double[] y3 = new double[]{3.0, 4.0, 5.0, 6.0};

        Assert.assertEquals(12.0, mannWhitneyU(x3, y3), 0.001);

        // Test with arrays containing NaN
        double[] x4 = new double[]{Double.NaN, 2.0, 3.0};
        double[] y4 = new double[]{1.0, Double.NaN, 3.0};

        try {
            mannWhitneyU(x4, y4);
            Assert.fail("Expected ConvergenceException but no exception was thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with arrays containing different sizes and null elements
        double[] x5 = new double[]{1.0, 2.0, null};
        double[] y5 = new double[]{1.0, 2.0, 3.0, null};

        try {
            mannWhitneyU(x5, y5);
            Assert.fail("Expected NullArgumentException but no exception was thrown.");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        // Test with two empty arrays
        double[] x = new double[0];
        double[] y = new double[0];

        try {
            mannWhitneyUTest(x, y);
            Assert.fail("Expected NoDataException but no exception was thrown.");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with two arrays of the same length and same values
        double[] x1 = new double[]{1.0, 2.0, 3.0};
        double[] y1 = new double[]{1.0, 2.0, 3.0};

        Assert.assertEquals(0.05, mannWhitneyUTest(x1, y1), 0.001);

        // Test with two arrays of the same length but different values
        double[] x2 = new double[]{1.0, 2.0, 3.0};
        double[] y2 = new double[]{3.0, 2.0, 1.0};

        Assert.assertEquals(0.01, mannWhitneyUTest(x2, y2), 0.001);

        // Test with two arrays of different lengths
        double[] x3 = new double[]{1.0, 2.0, 3.0};
        double[] y3 = new double[]{3.0, 4.0, 5.0, 6.0};

        Assert.assertEquals(0.0, mannWhitneyUTest(x3, y3), 0.001);

        // Test with arrays containing NaN
        double[] x4 = new double[]{Double.NaN, 2.0, 3.0};
        double[] y4 = new double[]{1.0, Double.NaN, 3.0};

        try {
            mannWhitneyUTest(x4, y4);
            Assert.fail("Expected ConvergenceException but no exception was thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with arrays containing different sizes and null elements
        double[] x5 = new double[]{1.0, 2.0, null};
        double[] y5 = new double[]{1.0, 2.0, 3.0, null};

        try {
            mannWhitneyUTest(x5, y5);
            Assert.fail("Expected NullArgumentException but no exception was thrown.");
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}