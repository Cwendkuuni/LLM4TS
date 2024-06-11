import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(5.0, normal.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(4.0);
        Assert.assertEquals(4.0, normal.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(2.0, normal.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(3.0);
        Assert.assertEquals(3.0, normal.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(0.3085375, normal.cumulativeProbability(4.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(3.228, normal.inverseCumulativeProbability(0.1), 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZero() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOne() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1.0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithZero() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegative() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(-1.0);
    }
}