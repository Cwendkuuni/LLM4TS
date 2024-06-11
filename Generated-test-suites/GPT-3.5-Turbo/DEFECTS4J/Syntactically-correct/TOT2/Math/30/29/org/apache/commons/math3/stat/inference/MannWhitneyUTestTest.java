import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        
        try {
            double result = testInstance.mannWhitneyUTest(x, y);
            assertEquals(0.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTestEdgeCase() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        
        double[] x = {1};
        double[] y = {1};
        
        try {
            double result = testInstance.mannWhitneyUTest(x, y);
            assertEquals(0.5, result, 0.0001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        
        try {
            double result = testInstance.mannWhitneyU(x, y);
            assertEquals(0.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUEdgeCase() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        
        double[] x = {1};
        double[] y = {1};
        
        try {
            double result = testInstance.mannWhitneyU(x, y);
            assertEquals(1.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestExceptionHandling() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        
        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        
        try {
            testInstance.mannWhitneyUTest(x, y);
            fail("Exception not thrown");
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            assertTrue(true);
        }
    }

}