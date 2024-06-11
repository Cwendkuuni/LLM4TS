import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.ConvergenceException;

public class MannWhitneyUTestTest {
    
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(result, 15.0, 0.001);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyU_NullArgument() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = null;
        
        try {
            test.mannWhitneyU(x, y);
            fail("Expected NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyU_NoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            test.mannWhitneyU(x, y);
            fail("Expected NoDataException was not thrown");
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NoDataException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(result, 1.0, 0.001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest_NullArgument() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = null;
        
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest_NoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected NoDataException was not thrown");
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NoDataException e) {
            assertNotNull(e.getMessage());
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest_ConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException was not thrown");
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            assertNotNull(e.getMessage());
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest_MaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected MaxCountExceededException was not thrown");
        } catch (NullArgumentException | NoDataException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MaxCountExceededException e) {
            assertNotNull(e.getMessage());
        }
    }
}