
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        Assert.assertEquals(2, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        Assert.assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.3989, distribution.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.8413, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1, distribution.inverseCumulativeProbability(0.8413), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        Assert.assertNotNull(sample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

}
