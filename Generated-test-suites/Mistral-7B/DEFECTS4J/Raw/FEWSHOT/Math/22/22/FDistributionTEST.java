package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        try {
            FDistribution fd = new FDistribution(3.0, 4.0);
            assertNotNull(fd);
            assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-3.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() {
        new FDistribution(0.0, 4.0);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double p = 0.1;
        double x = fd.inverseCumulativeProbability(p);
        double expected = 1.2335727455412312; // The result may vary slightly due to Math.random()
        assertEquals(expected, x, 0.001);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double x = 1.2;
        double expected = 0.1544123533389638; // The result may vary slightly due to Math.log()
        assertEquals(expected, fd.density(x), 0.001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double x = 1.2;
        double expected = 0.884288673258658; // The result may vary slightly due to Math.exp() and Beta.logBeta()
        assertEquals(expected, fd.cumulativeProbability(x), 0.001);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double expected = 1.5; // The result may vary slightly due to Math.sqrt() and Beta.logBeta()
        assertEquals(expected, fd.getNumericalMean(), 0.001);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double expected = 2.482628837143135; // The result may vary slightly due to Math.sqrt() and Beta.logBeta()
        assertEquals(expected, fd.getNumericalVariance(), 0.001);
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
        assertTrue(fd.isSupportConnected());
    }
}