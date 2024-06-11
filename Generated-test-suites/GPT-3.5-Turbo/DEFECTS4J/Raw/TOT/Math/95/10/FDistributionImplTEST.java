import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3, 4);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 2.0;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        assertEquals(0.808743, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeValue() throws MathException {
        double x = -1.0;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.6;
        double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(p);
        assertEquals(1.401343, inverseCumulativeProbability, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        double p = 0.0;
        double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(p);
        assertEquals(0.0, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
        double p = 1.0;
        double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.5;
        double domainLowerBound = fDistribution.getDomainLowerBound(p);
        assertEquals(0.0, domainLowerBound, 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.5;
        double domainUpperBound = fDistribution.getDomainUpperBound(p);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double initialDomain = fDistribution.getInitialDomain(p);
        assertEquals(1.333333, initialDomain, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithZeroValue() {
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithZeroValue() {
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }

}