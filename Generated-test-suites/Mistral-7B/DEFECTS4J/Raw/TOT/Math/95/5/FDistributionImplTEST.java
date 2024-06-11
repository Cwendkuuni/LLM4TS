package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;

/**
 * Test class for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        assertEquals("Numerator degrees of freedom set correctly.", 1.0,
                fd.getNumeratorDegreesOfFreedom(), 1e-12);
        assertEquals("Denominator degrees of freedom set correctly.", 2.0,
                fd.getDenominatorDegreesOfFreedom(), 1e-12);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        double cdf1 = fd.cumulativeProbability(0.5);
        double cdf2 = fd.cumulativeProbability(1.0);
        double cdf3 = fd.cumulativeProbability(2.0);
        assertEquals("CDF values correct for given x.", cdf1, 0.3989323, 1e-12);
        assertEquals("CDF values correct for given x.", cdf2, 0.880797, 1e-12);
        assertEquals("CDF values correct for given x.", cdf3, 0.9766586, 1e-12);

        // Test invalid input
        try {
            fd.cumulativeProbability(-1.0);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        double p1 = 0.1;
        double p2 = 0.5;
        double p3 = 0.9;

        double x1 = fd.inverseCumulativeProbability(p1);
        double x2 = fd.inverseCumulativeProbability(p2);
        double x3 = fd.inverseCumulativeProbability(p3);

        assertEquals("Inverse CDF values correct for given p.", x1, 1.564141, 1e-12);
        assertEquals("Inverse CDF values correct for given p.", x2, 0.837233, 1e-12);
        assertEquals("Inverse CDF values correct for given p.", x3, 0.359293, 1e-12);

        // Test invalid input
        try {
            fd.inverseCumulativeProbability(-1.0);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link FDistributionImpl#getDomainLowerBound(double)}.
     */
    @Test
    public void testGetDomainLowerBound() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        double p = 0.1;
        double lowerBound = fd.getDomainLowerBound(p);
        assertEquals("Lower bound values correct for given p.", lowerBound,
                Double.NaN);
    }

    /**
     * Test method for {@link FDistributionImpl#getDomainUpperBound(double)}.
     */
    @Test
    public void testGetDomainUpperBound() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        double p = 0.1;
        double upperBound = fd.getDomainUpperBound(p);
        assertEquals("Upper bound values correct for given p.", upperBound,
                Double.POSITIVE_INFINITY);
    }

    /**
     * Test method for {@link FDistributionImpl#getInitialDomain(double)}.
     */
    @Test
    public void testGetInitialDomain() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        double p = 0.1;
        double initialDomain = fd.getInitialDomain(p);
        assertEquals("Initial domain values correct for given p.", initialDomain,
                1.0 / (1.0 - 2.0 / 3.0), 1e-12);
    }
}