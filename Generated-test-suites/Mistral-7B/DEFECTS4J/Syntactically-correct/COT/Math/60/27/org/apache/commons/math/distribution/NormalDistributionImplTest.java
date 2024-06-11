import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_meanPositiveStandardDeviationPositive() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertNotNull(dist);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_meanNegative() {
        new NormalDistributionImpl(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_standardDeviationZero() {
        new NormalDistributionImpl(0.0, 0.0);
    }

    @Test
    public void testNormalDistributionImpl_density() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double density = dist.density(0.5);
        assertEquals(0.39894228040143273, density, 0.0000001);
    }

    @Test
    public void testNormalDistributionImpl_cumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double prob = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894921372413, prob, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_cumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(3.0);
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double inverseProb = dist.inverseCumulativeProbability(0.5);
        assertEquals(0.5, inverseProb, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_inverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testNormalDistributionImpl_sample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_sampleError() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.sample(10);
    }

    @Test
    public void testNormalDistributionImpl_getSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImpl_exceptionCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(3.5);
    }
}