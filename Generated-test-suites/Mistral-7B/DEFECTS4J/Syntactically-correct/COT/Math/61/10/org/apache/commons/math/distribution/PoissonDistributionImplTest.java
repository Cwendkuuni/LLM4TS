import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        assertEquals(Math.exp(-0.5), poissonDistribution.probability(0), 0.0);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        assertEquals(Math.exp(-2.5) * Math.pow(2.71828, 1), probability, 0.001);
    }

    @Test
    public void testProbabilityNegativeAndMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDistribution.probability(-1), 0.0);
        assertEquals(1.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDistribution.cumulativeProbability(0), 0.0);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.367978165251, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 0.0);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        assertEquals(poissonDistribution.cumulativeProbability(sample), 1.0, 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(Math.exp(-2.5), normalApproximateProbability, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(Math.exp(-2.5) * Math.pow(Math.E, 1), normalApproximateProbability, 0.01);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.0);
        assertEquals(0, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(1.0);
        assertEquals(1, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityHigherThanOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(1.1);
        assertTrue(inverseCumulativeProbability > 1);
    }
}