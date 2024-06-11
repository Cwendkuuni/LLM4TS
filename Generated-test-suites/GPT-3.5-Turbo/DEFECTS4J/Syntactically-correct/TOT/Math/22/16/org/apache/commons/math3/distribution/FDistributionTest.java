import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(5, 10);
    }

    @Test
    public void testDensity_typical() {
        double x = 2.5;
        double density = fDistribution.density(x);
        assertEquals(0.130708796, density, 1e-9);
    }

    @Test
    public void testDensity_edge() {
        double x = 0.01;
        double density = fDistribution.density(x);
        assertEquals(0.598790881, density, 1e-9);
    }

    @Test
    public void testDensity_error() {
        double x = -1;
        double density = fDistribution.density(x);
        assertEquals(0.0, density, 1e-9);
    }

    @Test
    public void testCumulativeProbability_typical() {
        double x = 2.5;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        assertEquals(0.905793516, cumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbability_edge() {
        double x = 0.01;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        assertEquals(0.015711109, cumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbability_error() {
        double x = -1;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numeratorDegreesOfFreedom, 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denominatorDegreesOfFreedom, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(10, numericalMean, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(400.0, numericalVariance, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }
}