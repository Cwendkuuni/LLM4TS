import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(5, distribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(2, distribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(0.1994711402, distribution.density(6), DELTA);
        Assert.assertEquals(0.0539909665, distribution.density(4), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(0.8413447460, distribution.cumulativeProbability(7), DELTA);
        Assert.assertEquals(0.1586552539, distribution.cumulativeProbability(3), DELTA);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(7.2897072539, distribution.inverseCumulativeProbability(0.9), DELTA);
        Assert.assertEquals(2.7102927460, distribution.inverseCumulativeProbability(0.1), DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, -2);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxIterationsExceeded() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        distribution.cumulativeProbability(40);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        distribution.inverseCumulativeProbability(1.5);
    }
}