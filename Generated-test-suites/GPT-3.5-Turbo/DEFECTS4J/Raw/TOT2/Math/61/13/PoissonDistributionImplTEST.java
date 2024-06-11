import org.junit.Test;
import org.apache.commons.math.MathException;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.3198, distribution.probability(2), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.1841, distribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.5255, distribution.normalApproximateProbability(2), 0.0001);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int sample = distribution.sample();
        boolean isValid = sample >= 0 && sample <= Integer.MAX_VALUE;
        assertEquals(true, isValid);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}