package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.stat.descript.moment.Mean;
import org.junit.Test;

public class PoissonDistributionImplTest {

    /**
     * Test constructors.
     */
    @Test
    public void testConstructors() {
        PoissonDistributionImpl pd1 = new PoissonDistributionImpl(1.5);
        PoissonDistributionImpl pd2 = new PoissonDistributionImpl(1.5, 1e-10, 1000000);
        PoissonDistributionImpl pd3 = new PoissonDistributionImpl(1.5, 1e-10);
        PoissonDistributionImpl pd4 = new PoissonDistributionImpl(1.5, 1000000);

        assertEquals(1.5, pd1.getMean(), 0.0);
        assertEquals(1.5, pd2.getMean(), 0.0);
        assertEquals(1.5, pd3.getMean(), 0.0);
        assertEquals(1.5, pd4.getMean(), 0.0);

        try {
            new PoissonDistributionImpl(-1.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        PoissonDistributionImpl pd5 = new PoissonDistributionImpl(0.0);
        assertEquals(0.0, pd5.getMean(), 0.0);
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);

        for (int i = 0; i <= 5; i++) {
            double p = pd.probability(i);
            double expected = Math.exp(-2.5) * Math.pow(2.5, i) / Math.factorial(i);
            assertEquals(expected, p, 1e-8);
        }

        try {
            pd.probability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            pd.probability(Integer.MAX_VALUE);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);

        for (int i = 0; i <= 5; i++) {
            double cp = pd.cumulativeProbability(i);
            double expected = Gamma.regularizedGammaQ((double) i + 1, 2.5);
            assertEquals(expected, cp, 1e-8);
        }

        try {
            pd.cumulativeProbability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            pd.cumulativeProbability(Integer.MAX_VALUE);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }
    }

    /**
     * Test normalApproximateProbability method.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);

        for (int i = 0; i <= 5; i++) {
            double appProb = pd.normalApproximateProbability(i);
            double prob = pd.probability(i);
            double expectedPoisson = Math.exp(-2.5) * Math.pow(2.5, i) / Math.factorial(i);
            double expectedNormal = FastMath.erf((i - 2.5) / Math.sqrt(2.5)) / 2.0;
            double tolerance = Math.max(Math.abs(expectedPoisson - 0.5), Math.abs(expectedNormal - 0.5));
            assertTrue(Math.abs(appProb - prob) < tolerance);
        }

        try {
            pd.normalApproximateProbability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            pd.normalApproximateProbability(Integer.MAX_VALUE);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        RandomData randomData = Mockito.mock(RandomData.class);
        when(randomData.nextPoisson(2.5)).thenCallRealMethod();
        PoissonDistribution pdMock = Mockito.spy(pd);
        pdMock.setRandomData(randomData);

        for (int i = 0; i < 1000; i++) {
            int sample = pdMock.sample();
            assertTrue(sample >= 0 && sample <= 1000 * 2.5);
        }

        verify(randomData, times(1000)).nextPoisson(2.5);
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            int expectedValue = (int) Math.round(pd.inverseCumulativeProbability(p));
            int actualValue = pd.inverseCumulativeProbability(p).intValue();
            assertEquals(expectedValue, actualValue);
        }

        try {
            pd.inverseCumulativeProbability(-0.01);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            pd.inverseCumulativeProbability(1.01);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }
    }

    /**
     * Test getMean method.
     */
    @Test
    public void testGetMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.0);
    }

    /**
     * Test getStandardDeviation method.
     */
    @Test
    public void testGetStandardDeviation() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        PoissonDistribution poissonDistribution = new PoissonDistribution(2.5);
        StandardDeviation stdDev = new StandardDeviation();
        double[] data = new double[10000];

        for (int i = 0; i < 10000; i++) {
            data[i] = pd.sample();
        }

        double calculatedStandardDeviation = stdDev.evaluate(data);
        double expectedStandardDeviation = Math.sqrt(2.5);
        assertEquals(expectedStandardDeviation, calculatedStandardDeviation, 0.01);

        double mean = new Mean().evaluate(data);
        assertEquals(pd.getMean(), mean, 0.01);
    }
}