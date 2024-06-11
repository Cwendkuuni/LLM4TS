import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.05263, distribution.probability(0), 0.0001);
        assertEquals(0.18421, distribution.probability(1), 0.0001);
        assertEquals(0.32436, distribution.probability(2), 0.0001);
        assertEquals(0.32436, distribution.probability(4), 0.0001);
        assertEquals(0.09731, distribution.probability(5), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.09731, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.49712, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(0.83152, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.49955, distribution.normalApproximateProbability(5), 0.0001);
        assertEquals(0.999999933, distribution.normalApproximateProbability(10000), 0.0001);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
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