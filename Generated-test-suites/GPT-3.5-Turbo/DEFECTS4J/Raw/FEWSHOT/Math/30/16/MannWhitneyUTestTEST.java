package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(0.0, test.mannWhitneyU(x, y), 0.0);
        
        x = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        y = new double[] {6.0, 7.0, 8.0, 9.0, 10.0};
        assertEquals(15.0, test.mannWhitneyU(x, y), 0.0);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArgumentException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUNoDataException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(1.0, test.mannWhitneyUTest(x, y), 0.0);
        
        x = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        y = new double[] {6.0, 7.0, 8.0, 9.0, 10.0};
        assertEquals(0.0, test.mannWhitneyUTest(x, y), 0.0);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0, 8.0, 9.0};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = new double[1000];
        double[] y = new double[1000];
        test.mannWhitneyUTest(x, y);
    }

}