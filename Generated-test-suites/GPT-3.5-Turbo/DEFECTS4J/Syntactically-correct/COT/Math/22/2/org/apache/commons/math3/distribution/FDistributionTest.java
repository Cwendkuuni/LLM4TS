import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5, 10);
        double density = fDist.density(2);
        assertEquals(0.054141552, density, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5, 10);
        double cumProb = fDist.cumulativeProbability(2);
        assertEquals(0.831971224040565, cumProb, 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        double degreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(5, degreesOfFreedom, 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        double degreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(10, degreesOfFreedom, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5, 10);
        double mean = fDist.getNumericalMean();
        assertEquals(1.25, mean, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5, 10);
        double variance = fDist.getNumericalVariance();
        assertEquals(1.875, variance, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(5, 10);
        double lowerBound = fDist.getSupportLowerBound();
        assertEquals(0, lowerBound, 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(5, 10);
        double upperBound = fDist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 10);
        boolean inclusive = fDist.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 10);
        boolean inclusive = fDist.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(5, 10);
        boolean connected = fDist.isSupportConnected();
        assertTrue(connected);
    }
}