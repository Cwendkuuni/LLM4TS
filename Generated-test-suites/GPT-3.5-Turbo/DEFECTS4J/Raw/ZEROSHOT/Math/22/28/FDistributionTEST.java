import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(2, 3);
        double density1 = distribution.density(1);
        assertEquals(0.4755282581475776, density1, 0.0001);

        double density2 = distribution.density(2);
        assertEquals(0.12635009704714493, density2, 0.0001);

        double density3 = distribution.density(3);
        assertEquals(0.034523985974415175, density3, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(2, 3);
        double cumulativeProbability1 = distribution.cumulativeProbability(1);
        assertEquals(0.5405405405405406, cumulativeProbability1, 0.0001);

        double cumulativeProbability2 = distribution.cumulativeProbability(2);
        assertEquals(0.7692307692307693, cumulativeProbability2, 0.0001);

        double cumulativeProbability3 = distribution.cumulativeProbability(3);
        assertEquals(0.8681318681318682, cumulativeProbability3, 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(2, 3);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(2, numeratorDegreesOfFreedom, 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(2, 3);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(3, denominatorDegreesOfFreedom, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(2, 3);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(Double.NaN, numericalMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(2, 3);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(Double.NaN, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(2, 3);
        double supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(2, 3);
        double supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(2, 3);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(2, 3);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(2, 3);
        boolean isSupportConnected = distribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}