import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU_typicalCase() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(6.0, result, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_edgeCase() {
        double[] x = {1};
        double[] y = {1};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(0.0, result, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_nullArguments() {
        double[] x = null;
        double[] y = null;
        try {
            double result = test.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_emptyArguments() {
        double[] x = {};
        double[] y = {};
        try {
            double result = test.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void testMannWhitneyUTest_typicalCase() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertTrue(result >= 0.0 && result <= 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_edgeCase() {
        double[] x = {1};
        double[] y = {1};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertTrue(result >= 0.0 && result <= 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_nullArguments() {
        double[] x = null;
        double[] y = null;
        try {
            double result = test.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_emptyArguments() {
        double[] x = {};
        double[] y = {};
        try {
            double result = test.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}