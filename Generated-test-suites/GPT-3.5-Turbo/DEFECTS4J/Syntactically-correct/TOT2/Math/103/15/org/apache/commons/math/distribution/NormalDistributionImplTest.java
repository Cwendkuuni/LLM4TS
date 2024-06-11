import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(10.0);
        assertEquals(10.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.6915, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(0.6915);
        assertEquals(0.5, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }
}