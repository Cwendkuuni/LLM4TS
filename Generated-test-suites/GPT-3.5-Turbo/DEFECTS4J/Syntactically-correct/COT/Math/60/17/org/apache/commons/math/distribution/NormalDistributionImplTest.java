import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double mean = normalDistribution.getMean();
        Assert.assertEquals(10, mean, 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double standardDeviation = normalDistribution.getStandardDeviation();
        Assert.assertEquals(2, standardDeviation, 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double density = normalDistribution.density(15);
        Assert.assertEquals(0.026995483, density, 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double cumulativeProbability = normalDistribution.cumulativeProbability(15);
        Assert.assertEquals(0.84134474, cumulativeProbability, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.84134474);
        Assert.assertEquals(15, inverseCumulativeProbability, 0.000001);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double sample = normalDistribution.sample();
        Assert.assertEquals(10, sample, 0);
    }
}