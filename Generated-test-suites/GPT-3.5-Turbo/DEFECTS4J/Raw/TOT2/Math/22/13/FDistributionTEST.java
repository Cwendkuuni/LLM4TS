import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {
    FDistribution fDistribution;

    @Test
    public void testDensity() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.04434095901055822, fDistribution.density(1.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.43363318673102016, fDistribution.cumulativeProbability(1.0), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(10.0 / 8.0, fDistribution.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(800.0 / 84.0, fDistribution.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(true, fDistribution.isSupportConnected());
    }
}