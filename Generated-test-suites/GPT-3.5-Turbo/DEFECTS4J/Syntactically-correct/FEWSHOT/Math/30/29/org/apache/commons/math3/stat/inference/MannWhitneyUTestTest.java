package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Test;

public class MannWhitneyUTestTest {
    private static final double EPSILON = 1e-15;

    @Test
    public void testSingletonVsEmpty() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {3.0};
        double[] y = {};

        assertEquals(0.0, test.mannWhitneyUTest(x, y), EPSILON);
    }

    @Test
    public void testEmptyVsSingleton() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {3.0};

        assertEquals(1.0, test.mannWhitneyUTest(x, y), EPSILON);
    }

    @Test
    public void testSingletonVsSingleton() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {2.0};
        double[] y = {2.0};

        assertEquals(0.5, test.mannWhitneyUTest(x, y), EPSILON);
    }

    @Test
    public void testSingletonVsSingletonDifferent() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0};
        double[] y = {2.0};

        assertEquals(0.0, test.mannWhitneyUTest(x, y), EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFirst() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {2.0};

        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSecond() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {2.0};
        double[] y = null;

        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEmptyFirst() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {2.0};

        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEmptySecond() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {2.0};
        double[] y = {};

        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testHighlySignificantRejection() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 1, 2, 2, 4, 4};
        double[] y = {3, 3, 5, 5, 7, 7};

        double p = test.mannWhitneyUTest(x, y);
        assertTrue(p < 0.001);
    }

    @Test
    public void testHighlySignificantRejection2() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {2,2,44,1};
        double[] y = {1,1,4,12};

        double p = test.mannWhitneyUTest(x, y);
        assertTrue(p < 0.001);
    }

    @Test
    public void testInsufficientData() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2};
        double[] y = {2, 3};

        try {
            test.mannWhitneyUTest(x, y);
            fail("Exception expected");
        } catch (NoDataException e) {
            // success
        }
    }

    @Test
    public void testTolerance() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        assertNotNull(test.calculateAsymptoticPValue(12000, 6, 6));
    }

    @Test
    public void testCompareU() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2};
        double[] y = {3, 4};

        double U1 = test.mannWhitneyU(x, y);
        double U2 = test.mannWhitneyU(y, x);

        assertEquals(U1, U2, EPSILON);
    }

    @Test
    public void testCompareP() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2};
        double[] y = {3, 4};

        double p1 = test.mannWhitneyUTest(x, y);
        double p2 = test.mannWhitneyUTest(y, x);

        assertEquals(p1, p2, EPSILON);
    }
}