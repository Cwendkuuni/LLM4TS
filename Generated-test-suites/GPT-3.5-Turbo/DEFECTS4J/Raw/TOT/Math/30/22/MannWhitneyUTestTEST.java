import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    // Test mannWhitneyU method

    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {2, 4, 6, 8, 10};

        try {
            double actualU = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(12.0, actualU, 0.0001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullY() throws NullArgumentException, NoDataException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = null;
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    // Test calculateAsymptoticPValue method

    @Test
    public void testCalculateAsymptoticPValue() {
        try {
            double actualPValue = mannWhitneyUTest.calculateAsymptoticPValue(12, 5, 5);
            assertEquals(0.364716, actualPValue, 0.000001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueWithConvergenceException() throws ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.calculateAsymptoticPValue(12, 0, 5);
    }

    // Test mannWhitneyUTest method

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {2, 4, 6, 8, 10};

        try {
            double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.771567, actualPValue, 0.000001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = null;
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5, 2, 4};
        double[] y = {2, 4, 6, 8, 10};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

}