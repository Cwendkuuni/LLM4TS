
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double DELTA = 1e-15;
    private NormalDistributionImpl normalDistribution;
    
    @Test
    public void testGetMean() {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(5.0, normalDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(3.0);
        Assert.assertEquals(3.0, normalDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(2.0, normalDistribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(4.0);
        Assert.assertEquals(4.0, normalDistribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(0.5, normalDistribution.cumulativeProbability(5.0), DELTA);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(5.0, normalDistribution.inverseCumulativeProbability(0.5), DELTA);
    }
}
