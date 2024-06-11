import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.distribution.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.FastMath;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl poissonDistribution;
    
    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(1.0);
    }
    
    @Test
    public void testGetMean() {
        Assert.assertEquals(1.0, poissonDistribution.getMean(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        PoissonDistributionImpl invalidMeanDistribution = new PoissonDistributionImpl(0.0);
    }
    
    @Test
    public void testProbability() {
        Assert.assertEquals(0.367879, poissonDistribution.probability(1), 0.0001);
        Assert.assertEquals(0.135335, poissonDistribution.probability(2), 0.0001);
        Assert.assertEquals(0.0497871, poissonDistribution.probability(3), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        Assert.assertEquals(0.367879, poissonDistribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.735758, poissonDistribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.866304, poissonDistribution.cumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        Assert.assertEquals(0.682689492137086, poissonDistribution.normalApproximateProbability(1), 0.0001);
        Assert.assertEquals(0.954499736103642, poissonDistribution.normalApproximateProbability(2), 0.0001);
        Assert.assertEquals(0.9973002039367404, poissonDistribution.normalApproximateProbability(3), 0.0001);
    }
    
    @Test
    public void testSample() throws MathException {
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        Assert.assertEquals(0, poissonDistribution.getDomainLowerBound(0.5));
    }
    
    @Test
    public void testGetDomainUpperBound() {
        Assert.assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
    }
    
    @After
    public void tearDown() {
        poissonDistribution = null;
    }
}