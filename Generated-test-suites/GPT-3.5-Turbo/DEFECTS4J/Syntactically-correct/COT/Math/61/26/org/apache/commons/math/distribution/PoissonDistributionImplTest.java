
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, distribution.getMean(), 0);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.06766764161830635, distribution.probability(0), 0);
        Assert.assertEquals(0.1403738958142805, distribution.probability(2), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.06766764161830635, distribution.cumulativeProbability(0), 0);
        Assert.assertEquals(0.14287653950145296, distribution.cumulativeProbability(2), 0);
    }
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.26845492223183403, distribution.normalApproximateProbability(0), 0);
        Assert.assertEquals(0.7891595211873731, distribution.normalApproximateProbability(2), 0);
    }
    
    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
