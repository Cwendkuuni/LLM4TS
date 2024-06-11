package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MannWhitneyUTestTest {
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(0, test.mannWhitneyU(x, y), 0.0001);
        
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 2.0, 1.0};
        assertEquals(9, test.mannWhitneyU(x2, y2), 0.0001);
        
        double[] x3 = {1.0, 2.0, 3.0};
        double[] y3 = {1.0, 2.0, 3.0};
        assertEquals(4.5, test.mannWhitneyU(x3, y3), 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(1, test.mannWhitneyUTest(x, y), 0.0001);
        
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 2.0, 1.0};
        assertEquals(0.1081, test.mannWhitneyUTest(x2, y2), 0.0001);
        
        double[] x3 = {1.0, 2.0, 3.0};
        double[] y3 = {1.0, 2.0, 3.0};
        assertEquals(0.5, test.mannWhitneyUTest(x3, y3), 0.0001);
    }
}