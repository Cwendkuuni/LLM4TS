import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest mannWhitneyUTest;
    
    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }
    
    @Test
    public void testMannWhitneyU_Asymptotic() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        
        double expectedPValue = 0.028;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        
        assertEquals(expectedPValue, actualPValue, 0.001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};
        
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = null;
        
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};
        
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {};
        
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
    
    @Test
    public void testMannWhitneyU_SameSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};
        
        double expectedPValue = 1.0;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        
        assertEquals(expectedPValue, actualPValue, 0.001);
    }
    
    @Test
    public void testMannWhitneyU_OneElementEach() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0};
        double[] y = {2.0};
        
        double expectedPValue = 0.0;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        
        assertEquals(expectedPValue, actualPValue, 0.001);
    }
    
}