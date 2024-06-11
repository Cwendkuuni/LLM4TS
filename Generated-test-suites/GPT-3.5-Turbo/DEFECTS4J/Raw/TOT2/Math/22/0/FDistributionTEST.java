import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.density(-1.0), 0.0001);
        assertEquals(0.0457, fDistribution.density(1.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(0.0521, fDistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        assertEquals(4.0, fDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(13.75, fDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(true, fDistribution.isSupportConnected());
    }
}