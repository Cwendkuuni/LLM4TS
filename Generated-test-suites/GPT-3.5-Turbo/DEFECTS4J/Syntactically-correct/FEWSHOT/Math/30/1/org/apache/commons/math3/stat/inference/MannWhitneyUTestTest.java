package org.apache.commons.math3.stat.inference;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest test = new MannWhitneyUTest();
    
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double result = test.mannWhitneyU(x, y);

        assertEquals(9.0, result, 0.0);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};

        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;

        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthX() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};

        test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};

        test.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double result = test.mannWhitneyUTest(x, y);

        assertNotNull(result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() throws Exception {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};

        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;

        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthX() throws Exception {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};

        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthY() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};

        test.mannWhitneyUTest(x, y);
    }
    
    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        double result = test.calculateAsymptoticPValue(5.0, 10, 15);

        assertNotNull(result);
    }
    
    @Test
    public void testCalculateAsymptoticPValueMaxInt() throws Exception {
        double result = test.calculateAsymptoticPValue(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertNotNull(result);
    }
    
    @Test
    public void testCalculateAsymptoticPValueMinInt() throws Exception {
        double result = test.calculateAsymptoticPValue(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        assertNotNull(result);
    }
    
    @Test
    public void testCalculateAsymptoticPValueMaxDouble() throws Exception {
        double result = test.calculateAsymptoticPValue(Double.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);

        assertNotNull(result);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueNonConverging() throws Exception {
        double result = test.calculateAsymptoticPValue(5.0, 0, 0);

        assertNull(result);
    }
}