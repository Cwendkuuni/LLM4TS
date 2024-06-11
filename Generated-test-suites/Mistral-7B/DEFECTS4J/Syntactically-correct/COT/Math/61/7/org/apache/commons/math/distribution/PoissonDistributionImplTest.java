import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-0.5);
    }

    @Test
    public void testProbabilityZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.probability(0);
        assertEquals(0.270485692314261, probability, 0.000001);
    }

    @Test
    public void testProbabilityPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.probability(2);
        assertEquals(0.154320533783329, probability, 0.000001);
    }

    @Test
    public void testProbabilityLarge() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(100.0);
        double probability = distribution.probability(100);
        assertEquals(0.339946041765109, probability, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = distribution.cumulativeProbability(1);
        assertEquals(0.367978164068525, cumulativeProbability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.normalApproximateProbability(0);
        assertEquals(0.244338421647244, probability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.5, probability, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertEquals(2, sample);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = distribution.cumulativeProbability(0);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(0, x);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = distribution.cumulativeProbability(1);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(1, x);
    }

    @Test
    public void testInverseCumulativeProbabilityPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = distribution.cumulativeProbability(0.5);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(1, x);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegative() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int x = distribution.inverseCumulativeProbability(-0.5);
    }
}