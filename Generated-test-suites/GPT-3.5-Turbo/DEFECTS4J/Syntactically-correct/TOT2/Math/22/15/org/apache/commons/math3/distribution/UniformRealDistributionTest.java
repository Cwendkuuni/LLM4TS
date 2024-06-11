import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.density(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(3, 6);
        assertEquals(3.0, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(6.0, distribution.getSupportUpperBound(), 0.0001);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBounds() {
        new UniformRealDistribution(5, 3);
    }

}