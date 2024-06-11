package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class MannWhitneyUTestCase {

    private static final double EPSILON = 1e-14;

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{-1.0, -2.0, 4.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, EPSILON);
    }

    @Test
    public void testMannWhitneyUTestNullArgumentException() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(new double[0], null));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, null));
    }

    @Test
    public void testMannWhitneyUTestNoDataException() {
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTestConvergenceException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        new Random().nextDouble(); // seed
        for (int i = 0; i < x.length; i++) {
            x[i] = new Random().nextDouble();
            y[i] = new Random().nextDouble();
        }
        assertThrows(ConvergenceException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTestMaxCountExceededException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        assertThrows(MaxCountExceededException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTestSameData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{1.0, 2.0, 3.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, EPSILON);
        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(1.0, asymptoticPValue, EPSILON);
    }

    @Test
    public void testMannWhitneyUTestDifferentData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(9.0, mannWhitneyU, EPSILON);
        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        // The actual value depends on the randomness of the data
        // but the result should be greater than 0.5
        assertEquals(0.5, asymptoticPValue, 0.1);
    }
}