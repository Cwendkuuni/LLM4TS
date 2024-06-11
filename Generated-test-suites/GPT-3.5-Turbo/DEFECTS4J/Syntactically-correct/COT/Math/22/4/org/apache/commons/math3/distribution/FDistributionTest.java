import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0.1449, fDistribution.density(0), 0.0001);
        assertEquals(0.2670, fDistribution.density(1), 0.0001);
        assertEquals(0.2670, fDistribution.density(2), 0.0001);
        assertEquals(0.1462, fDistribution.density(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0, fDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.1903, fDistribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.5441, fDistribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.7887, fDistribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        assertEquals(1, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        assertEquals(2, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        assertEquals(4, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        assertEquals(2, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        assertEquals(3, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        assertEquals(5, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution1 = new FDistribution(3, 4);
        assertEquals(2, fDistribution1.getNumericalMean(), 0.0001);

        FDistribution fDistribution2 = new FDistribution(2, 4);
        assert(Double.isNaN(fDistribution2.getNumericalMean()));

        FDistribution fDistribution3 = new FDistribution(4, 4);
        assertEquals(1.6, fDistribution3.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution1 = new FDistribution(3, 4);
        assertEquals(9.2307, fDistribution1.getNumericalVariance(), 0.0001);

        FDistribution fDistribution2 = new FDistribution(4, 4);
        assert(Double.isNaN(fDistribution2.getNumericalVariance()));

        FDistribution fDistribution3 = new FDistribution(5, 4);
        assertEquals(5.4545, fDistribution3.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertTrue(fDistribution.isSupportConnected());
    }

}