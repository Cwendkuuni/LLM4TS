package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test class for {@link MannWhitneyUTest}
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MEDIAN, TiesStrategy.MINIMUM);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        test.ensureDataConformance(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] z = test.concatenateSamples(x, y);
        assertEquals(x.length + y.length, z.length);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double mannWhitneyU = test.mannWhitneyU(x, y);
        assertEquals(2.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {3.5, 4.0, 4.5};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.16666666666666663, pValue, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 1.0;
        int n1 = 3;
        int n2 = 4;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.1625, pValue, 0.0);
    }
}