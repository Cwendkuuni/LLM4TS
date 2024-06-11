import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10, 20);
        double density = fDistribution.density(2.5);
        assertEquals(0.086802979, density, 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double cumulativeProb = fDistribution.cumulativeProbability(1.8);
        assertEquals(0.5626362, cumulativeProb, 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double numDF = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numDF, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double denomDF = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denomDF, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(7, 14);
        double mean = fDistribution.getNumericalMean();
        assertEquals(14.0/12.0, mean, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(8, 16);
        double variance = fDistribution.getNumericalVariance();
        assertEquals(4.0, variance, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3, 6);
        double lowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, lowerBound, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(6, 12);
        double upperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(4, 8);
        boolean inclusive = fDistribution.isSupportLowerBoundInclusive();
        assertEquals(true, inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(9, 18);
        boolean inclusive = fDistribution.isSupportUpperBoundInclusive();
        assertEquals(false, inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(10, 20);
        boolean connected = fDistribution.isSupportConnected();
        assertEquals(true, connected);
    }
}