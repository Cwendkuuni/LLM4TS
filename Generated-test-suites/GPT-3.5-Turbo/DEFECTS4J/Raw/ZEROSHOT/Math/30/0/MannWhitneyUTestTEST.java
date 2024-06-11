import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }
    
    @Test
    public void testMannWhitneyU() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {4.0, 5.0, 6.0};

            double result = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(9.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testMannWhitneyU_NullArgumentException() {
        try {
            double[] x = null;
            double[] y = {4.0, 5.0, 6.0};

            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        } catch (NoDataException e) {
            fail("NoDataException should not be thrown");
        }
    }
    
    @Test
    public void testMannWhitneyU_NoDataException() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {};

            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("NoDataException should be thrown");
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        } catch (NoDataException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {4.0, 5.0, 6.0};

            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException | 
                ConvergenceException | MaxCountExceededException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_NullArgumentException() {
        try {
            double[] x = null;
            double[] y = {4.0, 5.0, 6.0};

            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        } catch (NoDataException | ConvergenceException | 
                MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_NoDataException() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {};

            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("NoDataException should be thrown");
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        } catch (NoDataException e) {
            // Expected exception
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_ConvergenceException() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {4.0, 5.0, 6.0, 7.0, 8.0};

            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("ConvergenceException should be thrown");
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception thrown");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_MaxCountExceededException() {
        try {
            double[] x = {1.0, 2.0, 3.0};
            double[] y = {4.0, 5.0, 6.0};

            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("MaxCountExceededException should be thrown");
        } catch (NullArgumentException | NoDataException | 
                ConvergenceException e) {
            fail("Unexpected exception thrown");
        } catch (MaxCountExceededException e) {
            // Expected exception
        }
    }

}