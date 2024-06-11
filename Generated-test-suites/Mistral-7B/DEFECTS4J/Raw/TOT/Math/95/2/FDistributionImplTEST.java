package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;

/**
 * Test class for FDistributionImpl.
 */
public class FDistributionImplTest {

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}.
     */
    @Test
    public void testFDistributionImpl() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.0;
        double expected = Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom,
                0.5 * denominatorDegreesOfFreedom);
        double actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-12);

        x = 0.0;
        expected = 0.0;
        actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual);

        x = Double.POSITIVE_INFINITY;
        expected = 1.0;
        actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityLessThanZero() throws MathException {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double p = -0.1;
        fDist.inverseCumulativeProbability(p);
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double p = 0.1;
        double expected = ((numeratorDegreesOfFreedom * p) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom)) * (denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2.0));
        double actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-12);

        p = 0.5;
        expected = ((numeratorDegreesOfFreedom * p) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom)) * (denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2.0));
        actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-12);

        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newNumeratorDegreesOfFreedom = 3.0;
        fDist.setNumeratorDegreesOfFreedom(newNumeratorDegreesOfFreedom);
        assertEquals(newNumeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newDenominatorDegreesOfFreedom = 5.0;
        fDist.setDenominatorDegreesOfFreedom(newDenominatorDegreesOfFreedom);
        assertEquals(newDenominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}