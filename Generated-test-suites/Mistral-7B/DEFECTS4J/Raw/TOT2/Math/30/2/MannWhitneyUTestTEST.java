import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testMannWhitneyU_EmptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyU_NullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(null, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
        try {
            test.mannWhitneyU(new double[0], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyU_SameArray() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = x.clone();

        double result = test.mannWhitneyU(x, y);
        double expected = 3.0 * 4.0;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testMannWhitneyU_DifferentArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double result = test.mannWhitneyU(x, y);
        double expected = 6.0 * 6.0;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testMannWhitneyUTest_EmptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_NullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(null, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
        try {
            test.mannWhitneyUTest(new double[0], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_SameArray() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = x.clone();

        double result = test.mannWhitneyUTest(x, y);
        double expected = 1.0;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testMannWhitneyUTest_DifferentArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double result = test.mannWhitneyUTest(x, y);
        double expected = 2.0;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testMannWhitneyUTest_Ties() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.LARGEST_TIES);
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0};
        double[] y = {1.0, 2.0, 2.0, 2.0, 3.0};

        double result = test.mannWhitneyUTest(x, y);
        double expected = 5.0 * 5.0;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testMannWhitneyUTest_NaNValues() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.LARGEST_TIES);
        double[] x = {1.0, 2.0, Double.NaN, 3.0, 3.0};
        double[] y = {1.0, 2.0, Double.NaN, 2.0, 3.0};

        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // expected, since Mann-Whitney U test is not defined for NaN values
        } catch (MaxCountExceededException e) {
            // expected, since Mann-Whitney U test is not defined for NaN values
        }
    }
}