import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(2.5, poisson.getMean(), 0.0001);
    }

    // Test case for the probability method
    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(0.0820849986, poisson.probability(2), 0.0001);
        assertEquals(0.0163687392, poisson.probability(0), 0.0001);
        assertEquals(0.3633228896, poisson.probability(4), 0.0001);
    }

    // Test case for the cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(0.7788007831, poisson.cumulativeProbability(3), 0.0001);
        assertEquals(0.3233235838, poisson.cumulativeProbability(1), 0.0001);
        assertEquals(0.9810118431, poisson.cumulativeProbability(6), 0.0001);
    }

    // Test case for the normalApproximateProbability method
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(0.89435, poisson.normalApproximateProbability(3), 0.0001);
        assertEquals(0.91924, poisson.normalApproximateProbability(1), 0.0001);
        assertEquals(0.77812, poisson.normalApproximateProbability(6), 0.0001);
    }

    // Test case for the sample method
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        int sample = poisson.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    // Test case for the getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(0, poisson.getDomainLowerBound(0.5));
        assertEquals(0, poisson.getDomainLowerBound(0));
    }

    // Test case for the getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0));
    }
}