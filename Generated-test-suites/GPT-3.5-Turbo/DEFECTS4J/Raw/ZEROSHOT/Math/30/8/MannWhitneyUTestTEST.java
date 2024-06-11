import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArgument() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = null;
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyArray() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 2.0;
        int n1 = 3;
        int n2 = 3;

        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArgument() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = null;
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArray() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }
}