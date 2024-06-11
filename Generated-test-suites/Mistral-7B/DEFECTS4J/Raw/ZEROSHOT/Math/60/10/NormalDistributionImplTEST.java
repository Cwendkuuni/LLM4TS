import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertNotNull(dist.density(0.0));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1.0);
    }

    @Test
    public void testZeroStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(0.0, dist.getStandardDeviation(), 0.0);
        assertEquals(1.0, dist.density(0.0), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testNonZeroMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, dist.getMean(), 0.0);
        assertEquals(2.5, dist.getStandardDeviation(), 0.0);
        assertEquals(0.0656468657, dist.density(1.5), 0.0001);
        assertEquals(0.5, dist.cumulativeProbability(1.5), 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithInsideValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0.5);
        assertEquals(0.682689462137, result, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithOutsideValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(5.0);
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithTooFarOutsideValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithProbabilityBetweenZeroAndOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.01);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithOutsideProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInverseCumulativeProbabilityWithTooLowProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }
}