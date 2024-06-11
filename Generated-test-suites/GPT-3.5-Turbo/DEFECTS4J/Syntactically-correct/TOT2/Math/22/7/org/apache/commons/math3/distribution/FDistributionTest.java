import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.03958456874693008, fDistribution.density(2), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(8, 6);
        assertEquals(0.05, fDistribution.cumulativeProbability(0.2812), 1e-10);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 6);
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 6);
        assertEquals(6, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(4, 8);
        assertEquals(8.0 / 6.0, fDistribution.getNumericalMean(), 1e-10);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 15);
        assertEquals(150.0, fDistribution.getNumericalVariance(), 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2, 9);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(4, 6);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

}