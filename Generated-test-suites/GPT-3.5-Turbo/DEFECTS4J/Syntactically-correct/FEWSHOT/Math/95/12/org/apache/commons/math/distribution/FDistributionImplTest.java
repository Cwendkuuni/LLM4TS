import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistributionImpl(3, 5);

        try {
            assertEquals(0.0, distribution.cumulativeProbability(-1.0), EPSILON);
            assertEquals(0.0, distribution.cumulativeProbability(0.0), EPSILON);
            assertEquals(0.05166354574038451, distribution.cumulativeProbability(1.0), EPSILON);
            assertEquals(0.13542300010867332, distribution.cumulativeProbability(2.0), EPSILON);
            assertEquals(0.21890265484972283, distribution.cumulativeProbability(3.0), EPSILON);
            assertEquals(0.29885504662351883, distribution.cumulativeProbability(4.0), EPSILON);
            assertEquals(0.38297024255667053, distribution.cumulativeProbability(5.0), EPSILON);
            assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), EPSILON);
        } catch (MathException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistribution distribution = new FDistributionImpl(3, 5);

        try {
            assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), EPSILON);
            assertEquals(0.9488975379720518, distribution.inverseCumulativeProbability(0.5), EPSILON);
            assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), EPSILON);
        } catch (MathException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(0.0, distribution.getDomainLowerBound(0.0), EPSILON);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0.0, distribution.getDomainLowerBound(1.0), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertTrue(Double.isInfinite(distribution.getDomainUpperBound(0.0)));
        assertTrue(Double.isInfinite(distribution.getDomainUpperBound(0.5)));
        assertTrue(Double.isInfinite(distribution.getDomainUpperBound(1.0)));
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(1.6666666666666667, distribution.getInitialDomain(0.0), EPSILON);
        assertEquals(1.6666666666666667, distribution.getInitialDomain(0.5), EPSILON);
        assertEquals(1.6666666666666667, distribution.getInitialDomain(1.0), EPSILON);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        distribution.setNumeratorDegreesOfFreedom(10);

        assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        distribution.setNumeratorDegreesOfFreedom(-3);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        distribution.setDenominatorDegreesOfFreedom(8);

        assertEquals(8.0, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        distribution.setDenominatorDegreesOfFreedom(-5);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }
}