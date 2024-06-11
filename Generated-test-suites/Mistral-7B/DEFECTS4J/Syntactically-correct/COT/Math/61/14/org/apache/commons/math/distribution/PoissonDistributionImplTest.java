import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonPositiveMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.probability(0);
        assertEquals(0.6065120986959523, probability, 0.0001);
    }

    @Test
    public void testProbabilityPositiveInteger() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(3);
        assertEquals(0.2165547833746793, probability, 0.0001);
    }

    @Test
    public void testProbabilityNegativeInteger() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(-1);
        assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testProbabilityMaxInteger() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityMaxInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityPositiveInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = distribution.cumulativeProbability(3);
        assertEquals(0.9973125831635134, cumulativeProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        distribution.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.normalApproximateProbability(0);
        assertEquals(0.596446784422521, probability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityMaxInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityPositiveInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.normalApproximateProbability(3);
        assertEquals(0.2089428151235756, probability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityNegativeInteger() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        distribution.normalApproximateProbability(-1);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        int sample = distribution.sample();
        assertEquals(3, sample);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double p = distribution.cumulativeProbability(0);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(0, x);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double p = distribution.cumulativeProbability(Integer.MAX_VALUE);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE, x);
    }

    @Test
    public void testInverseCumulativeProbabilityPositiveProbability() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double p = distribution.cumulativeProbability(3);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(3, x);
    }

    @Test
    public void testInverseCumulativeProbabilityNegativeProbability() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double p = distribution.cumulativeProbability(2);
        double expected = Gamma.regularizedGammaQ(2.0, 0.5, 1e-12, 10000000);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals((int) Math.round(expected), x);
    }
}