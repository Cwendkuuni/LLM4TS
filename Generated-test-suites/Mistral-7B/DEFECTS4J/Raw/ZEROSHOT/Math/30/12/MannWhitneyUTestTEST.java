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
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() throws Exception {
        // Test case 1: Two equal samples
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {1.0, 2.0, 3.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double result1 = mannWhitneyUTest.mannWhitneyUTest(x1, y1);
        double expected1 = 3.0;
        assertEquals(expected1, result1, 0.001);

        // Test case 2: Two different samples with no significant difference
        double[] x2 = {1.5, 1.6, 1.7};
        double[] y2 = {1.55, 1.65, 1.75};

        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest();
        double result2 = mannWhitneyUTest2.mannWhitneyUTest(x2, y2);
        double expected2 = 3.0;
        assertEquals(expected2, result2, 0.001);

        // Test case 3: Two different samples with significant difference
        double[] x3 = {1.0, 2.3, 3.0};
        double[] y3 = {1.5, 2.0, 2.5};

        MannWhitneyUTest mannWhitneyUTest3 = new MannWhitneyUTest();
        double result3 = mannWhitneyUTest3.mannWhitneyUTest(x3, y3);
        double expected3 = 1.5;
        assertEquals(expected3, result3, 0.001);

        // Test case 4: Handling NaN values
        double[] x4 = {1.0, 2.0, Double.NaN, 3.0};
        double[] y4 = {1.5, 2.0, 2.5, Double.NaN};

        MannWhitneyUTest mannWhitneyUTest4 = new MannWhitneyUTest(
                NaNStrategy.REMOVE_MASKED, TiesStrategy.MIN);
        try {
            mannWhitneyUTest4.mannWhitneyUTest(x4, y4);
            fail();
        } catch (Exception e) {
            // Expected outcome
        }

        MannWhitneyUTest mannWhitneyUTest5 = new MannWhitneyUTest(
                NaNStrategy.REPLACE_WITH_MEDIAN, TiesStrategy.MIN);
        double result5 = mannWhitneyUTest5.mannWhitneyUTest(x4, y4);
        double expected5 = 4.5;
        assertEquals(expected5, result5, 0.001);

        // Test case 5: Handling empty samples
        double[] x5 = {};
        double[] y5 = {};

        MannWhitneyUTest mannWhitneyUTest6 = new MannWhitneyUTest();
        try {
            mannWhitneyUTest6.mannWhitneyUTest(x5, y5);
            fail();
        } catch (NoDataException e) {
            // Expected outcome
        }

        // Test case 6: Handling null input
        double[] x6 = null;
        double[] y6 = {1.0, 2.0, 3.0};

        MannWhitneyUTest mannWhitneyUTest7 = new MannWhitneyUTest();
        try {
            mannWhitneyUTest7.mannWhitneyUTest(x6, y6);
            fail();
        } catch (NullArgumentException e) {
            // Expected outcome
        }

        double[] y6_null = null;
        try {
            mannWhitneyUTest7.mannWhitneyUTest(x5, y6_null);
            fail();
        } catch (NullArgumentException e) {
            // Expected outcome
        }
    }
}