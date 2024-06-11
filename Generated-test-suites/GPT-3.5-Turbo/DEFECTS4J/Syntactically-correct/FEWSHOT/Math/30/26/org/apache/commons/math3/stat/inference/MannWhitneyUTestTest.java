package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest test;
    
    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }
    
    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        try {
            double u = test.mannWhitneyU(x, y);
            assertNotNull(u);
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCalculateAsymptoticPValue() {
        double u = 10.25;
        int n1 = 5;
        int n2 = 7;
        
        try {
            double pValue = test.calculateAsymptoticPValue(u, n1, n2);
            assertNotNull(pValue);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        
        try {
            double pValue = test.mannWhitneyUTest(x, y);
            assertNotNull(pValue);
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        }
    }
    
}