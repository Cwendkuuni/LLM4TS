import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    //Test case for probability method for x = 0
    @Test
    public void testProbabilityX0() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double probability = dist.probability(0);
        Assert.assertEquals(0.049787068367863944, probability, 0.000001);
    }
    
    //Test case for probability method for x = 5
    @Test
    public void testProbabilityX5() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double probability = dist.probability(5);
        Assert.assertEquals(0.10081881344492453, probability, 0.000001);
    }
    
    //Test case for cumulativeProbability method for x = 0
    @Test
    public void testCumulativeProbabilityX0() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double cumulativeProbability = dist.cumulativeProbability(0);
        Assert.assertEquals(0.04978706836786395, cumulativeProbability, 0.000001);
    }
    
    //Test case for cumulativeProbability method for x = 5
    @Test
    public void testCumulativeProbabilityX5() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double cumulativeProbability = dist.cumulativeProbability(5);
        Assert.assertEquals(0.18473675547622767, cumulativeProbability, 0.000001);
    }
    
    //Test case for normalApproximateProbability method for x = 0
    @Test
    public void testNormalApproximateProbabilityX0() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double normalApproximateProbability = dist.normalApproximateProbability(0);
        Assert.assertEquals(0.016057531049007175, normalApproximateProbability, 0.000001);
    }
    
    //Test case for normalApproximateProbability method for x = 5
    @Test
    public void testNormalApproximateProbabilityX5() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double normalApproximateProbability = dist.normalApproximateProbability(5);
        Assert.assertEquals(0.16857239271273138, normalApproximateProbability, 0.000001);
    }
    
    //Test case for sample method
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        int sample = dist.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }
}