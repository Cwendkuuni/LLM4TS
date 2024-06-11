import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        Assert.assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidStandardDeviation() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testDensity() {
        Assert.assertEquals(0.3989422804014327, distribution.density(0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        Assert.assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        distribution.inverseCumulativeProbability(-1);
    }

    @Test
    public void testSample() throws MathException {
        // Cannot test the actual value since it's random
        Assert.assertNotNull(distribution.sample());
    }
}