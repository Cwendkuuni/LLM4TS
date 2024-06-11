
package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.BeforeClass;
import org.junit.Test;

public class MannWhitneyUTestTest {
    
    private static MannWhitneyUTest test;
    
    @BeforeClass
    public static void setUpClass() {
        test = new MannWhitneyUTest();
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NoDataException, NullArgumentException {
        double[] x = null;
        double[] y = {1, 2, 3};
        
        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NoDataException, NullArgumentException {
        double[] x = {1, 2, 3};
        double[] y = null;
        
        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NoDataException, NullArgumentException {
        double[] x = {};
        double[] y = {1, 2, 3};
        
        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NoDataException, NullArgumentException {
        double[] x = {1, 2, 3};
        double[] y = {};
        
        test.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};

        double actual = test.mannWhitneyU(x, y);
        double expected = 0;

        assertEquals(expected, actual, 0.001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {1, 2, 3};
        
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = null;
        
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1, 2, 3};
        
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {};
        
        test.mannWhitneyUTest(x, y);
    }
    
    @Test
    public void testMannWhitneyUTest() throws NoDataException, NullArgumentException, 
            ConvergenceException, MaxCountExceededException {
        double[] x = {1,2,3};
        double[] y = {4,5,6};
        
        double actual = test.mannWhitneyUTest(x, y);
        assertNotNull(actual);
    }
    
    @Test
    public void testMannWhitneyUTest_TwoSided() throws NoDataException, NullArgumentException, 
            ConvergenceException, MaxCountExceededException {
        double[] x = {1,2,3};
        double[] y = {4,5,6};
        
        double actual = test.mannWhitneyUTest(x, y);
        double expected = 1;
        
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testMannWhitneyUTest_OneSided() throws NoDataException, NullArgumentException, 
            ConvergenceException, MaxCountExceededException {
        double[] x = {1,2,3};
        double[] y = {4,5,6};
        
        double actual = test.mannWhitneyUTest(x, y) / 2;
        double expected = 0.5;
        
        assertEquals(expected, actual, 0.001);
    }
    
}
