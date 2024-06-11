package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        // Test default constructor
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(0.0, mannWhitneyUTest.mannWhitneyUTest(new double[]{}, new double[]{}), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, mannWhitneyUTest.mannWhitneyUTest(new double[]{1.0}, new double[]{1.0}), 0.0);
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(null, new double[]{1.0}));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[]{1.0}, null));
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[]{1.0}, new double[]{}));
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[]{}, new double[]{1.0}));

        // Test constructor with custom strategies
        MannWhitneyUTest mannWhitneyUTestCustom = new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.MIN);
        double[] x = {1.0, 1.0, Double.NaN, 1.0};
        double[] y = {1.0, 1.0, 1.0, 1.0};
        assertEquals(0.0, mannWhitneyUTestCustom.mannWhitneyUTest(x, y), 0.0);
        assertEquals(2.0, mannWhitneyUTestCustom.mannWhitneyUTest(x, y), Precision.safeEpsilon);
        x = new double[]{1.0, 1.0, Double.NaN, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        assertEquals(3.0, mannWhitneyUTestCustom.mannWhitneyUTest(x, y), Precision.safeEpsilon);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.0, 1.5, 3.5};
        assertEquals(2.5, mannWhitneyUTest.mannWhitneyU(x, y), Precision.safeEpsilon);
        x = new double[]{1.0, 2.0, 3.0, 3.0};
        y = new double[]{1.0, 1.5, 3.5, 3.5};
        assertEquals(5.0, mannWhitneyUTest.mannWhitneyU(x, y), Precision.safeEpsilon);
        x = new double[]{-1.0, 2.0, 3.0};
        y = new double[]{1.0, 1.5, 3.5};
        assertEquals(4.5, mannWhitneyUTest.mannWhitneyU(x, y), Precision.safeEpsilon);
        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{1.0, 1.5, 3.5, 3.5};
        assertEquals(6.0, mannWhitneyUTest.mannWhitneyU(x, y), Precision.safeEpsilon);
    }

    @Test
    public void testAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(5.0, 10, 5);
        assertEquals(0.025, pValue, 0.001);
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(12.0, 15, 5);
        assertEquals(0.012, pValue, 0.001);
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(10.0, 15, 5);
        assertEquals(0.051, pValue, 0.001);
    }

}