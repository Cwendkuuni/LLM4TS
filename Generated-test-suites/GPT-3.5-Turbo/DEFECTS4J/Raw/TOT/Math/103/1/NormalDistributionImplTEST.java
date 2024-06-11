import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        double newMean = 5.0;
        distribution.setMean(newMean);
        assertEquals(newMean, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        double invalidSD = -1.0;
        distribution.setStandardDeviation(invalidSD);
    }

    @Test
    public void testSetStandardDeviation() {
        double newSD = 2.5;
        distribution.setStandardDeviation(newSD);
        assertEquals(newSD, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.0;
        double expectedProb = 0.8413;
        double actualProb = distribution.cumulativeProbability(x);
        assertEquals(expectedProb, actualProb, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForZero() throws MathException {
        double p = 0.0;
        double expectedVal = Double.NEGATIVE_INFINITY;
        double actualVal = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForOne() throws MathException {
        double p = 1.0;
        double expectedVal = Double.POSITIVE_INFINITY;
        double actualVal = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetDomainLowerBoundForP() {
        double p = 0.3;
        double expectedVal = Double.NEGATIVE_INFINITY;
        double actualVal = distribution.getDomainLowerBound(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetDomainLowerBoundForHalfP() {
        double p = 0.5;
        double expectedVal = 0.0;
        double actualVal = distribution.getDomainLowerBound(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetDomainUpperBoundForP() {
        double p = 0.3;
        double expectedVal = 0.0;
        double actualVal = distribution.getDomainUpperBound(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetDomainUpperBoundForHalfP() {
        double p = 0.5;
        double expectedVal = Double.POSITIVE_INFINITY;
        double actualVal = distribution.getDomainUpperBound(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetInitialDomainForP() {
        double p = 0.3;
        double expectedVal = -1.0;
        double actualVal = distribution.getInitialDomain(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }

    @Test
    public void testGetInitialDomainForHalfP() {
        double p = 0.5;
        double expectedVal = 0.0;
        double actualVal = distribution.getInitialDomain(p);
        assertEquals(expectedVal, actualVal, 0.0001);
    }
}