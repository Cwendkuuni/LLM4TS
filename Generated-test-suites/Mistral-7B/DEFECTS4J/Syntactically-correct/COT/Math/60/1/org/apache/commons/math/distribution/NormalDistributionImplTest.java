import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithValidArguments() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithInvalidArguments() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.density(0);
        assertEquals(0.398942280401433, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0);
        assertEquals(0.5, result, 0.0001);

        try {
            dist.cumulativeProbability(1000);
        } catch (MathException e) {
            assertEquals(0, result, 0.0001);
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals(0, sample, 0.001); // The sample should be close to mean but not equal
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityLessThanZero() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityGreaterThanOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(2);
    }

    @Test
    public void testInverseCumulativeProbabilityExact() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.75;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(0.674489753588452, result, 0.0001);
    }

    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(1, dist.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(2, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInverseCumulativeProbabilityWithLargeValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.5 * Math.pow(2, 30));
    }
}