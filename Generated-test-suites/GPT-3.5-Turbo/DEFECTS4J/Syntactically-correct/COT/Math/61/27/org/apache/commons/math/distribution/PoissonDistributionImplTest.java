import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        assertEquals(0.01831563888873418, distribution.probability(2), 1e-6);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.4404932850652127, distribution.cumulativeProbability(3), 1e-6);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.6167076708717037, distribution.normalApproximateProbability(9), 1e-6);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(8.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}