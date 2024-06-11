import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.046726, fDistribution.density(2.0), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.4502, fDistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(10.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(2.5, fDistribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(3.75, fDistribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

}