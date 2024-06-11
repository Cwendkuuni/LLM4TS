import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 9;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws Exception {
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws Exception {
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 0.216970;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws Exception {
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws Exception {
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTest_ConvergenceError() throws Exception {
        double[] x = {1, 1, 1};
        double[] y = {2, 2, 2};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTest_MaxIterationsExceeded() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest_MultipleRuns() throws Exception {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};

        double expected = 0.216970;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.0001);

        expected = 0.216970;
        actual = test.mannWhitneyUTest(y, x);
        assertEquals(expected, actual, 0.0001);
    }
}