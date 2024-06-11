import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    FDistribution distribution;

    @Test
    public void testDensity() {
        distribution = new FDistribution(5, 10);
        assertEquals(0.14285714285714285, distribution.density(2), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        distribution = new FDistribution(5, 10);
        assertEquals(0.4980608240919484, distribution.cumulativeProbability(2), 1e-15);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        distribution = new FDistribution(5, 10);
        assertEquals(5, distribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        distribution = new FDistribution(5, 10);
        assertEquals(10, distribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        distribution = new FDistribution(5, 10);
        assertEquals(1.25, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        distribution = new FDistribution(5, 10);
        assertEquals(10.783333333333333, distribution.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        distribution = new FDistribution(5, 10);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        distribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        distribution = new FDistribution(5, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        distribution = new FDistribution(5, 10);
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        distribution = new FDistribution(5, 10);
        assertTrue(distribution.isSupportConnected());
    }
}