import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        // Basic test for density method
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.08773499525781320, fDistribution.density(2.5), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        // Basic test for cumulativeProbability method
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.363456745644, fDistribution.cumulativeProbability(1.5), 1e-12);
    }

    @Test
    public void testGetNumericalMean() {
        // Basic test for getNumericalMean method
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(1.25, fDistribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        // Basic test for getNumericalVariance method
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(3.125, fDistribution.getNumericalVariance(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidParams() {
        // Test constructor with invalid parameters
        new FDistribution(0, 0);
    }

    @Test
    public void testGetSupport() {
        // Test getSupportLowerBound and getSupportUpperBound methods
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

}