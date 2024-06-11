import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {
    
    @Test
    public void testMannWhitneyU_BothSamplesNotEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        try {
            double U = uTest.mannWhitneyU(x, y);
            assertEquals(0.0, U, 0.0);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyU_OneSampleEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4, 5, 6};
        
        try {
            double U = uTest.mannWhitneyU(x, y);
            assertEquals(0.0, U, 0.0);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyU_BothSamplesEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        
        try {
            double U = uTest.mannWhitneyU(x, y);
            assertEquals(0.0, U, 0.0);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSample() throws NullArgumentException, NoDataException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        
        uTest.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_ZeroLengthSample() throws NullArgumentException, NoDataException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        
        uTest.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyUTest_BothSamplesNotEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        try {
            double pValue = uTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 0.0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_OneSampleEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4, 5, 6};
        
        try {
            double pValue = uTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 0.0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest_BothSamplesEmpty() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        
        try {
            double pValue = uTest.mannWhitneyUTest(x, y);
            assertEquals(0.5, pValue, 0.0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSample() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        
        uTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_ZeroLengthSample() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        
        uTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTest_ConvergenceError() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] y = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        
        uTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTest_MaxIterationsExceeded() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double[] y = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        
        uTest.mannWhitneyUTest(x, y);
    }
}