import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    // Test mannWhitneyU() method

    @Test
    public void testMannWhitneyUWithValidInputs() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(9.0, result, 0.00001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testMannWhitneyUWithNullInputs() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUWithEmptyInputs() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("NoDataException should be thrown");
        } catch (NoDataException e) {
            // Pass
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }

    // Test mannWhitneyUTest() method

    @Test
    public void testMannWhitneyUTestWithValidInputs() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.26955, result, 0.00001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestWithNullInputs() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestWithEmptyInputs() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("NoDataException should be thrown");
        } catch (NoDataException e) {
            // Pass
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestWithConvergenceError() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("ConvergenceException should be thrown");
        } catch (ConvergenceException e) {
            // Pass
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }
}