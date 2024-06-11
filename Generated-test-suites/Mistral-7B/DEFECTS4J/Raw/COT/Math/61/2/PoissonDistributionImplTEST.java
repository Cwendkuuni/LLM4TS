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
        assertEquals(0.6065162865645898, poissonDistribution.probability(0), 0.0000001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        assertEquals(0.22314242311785835, probability, 0.0000001);
    }

    @Test
    public void testProbabilityMaxInt() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.0);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        assertEquals(0.0, poissonDistribution.cumulativeProbability(0), 0.0);
    }

    @Test
    public void testCumulativeProbabilityPositiveLessThanMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.3679243445211635, cumulativeProbability, 0.0000001);
    }

    @Test
    public void testCumulativeProbabilityPositiveEqualMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.6804549137631941, cumulativeProbability, 0.0000001);
    }

    @Test
    public void testCumulativeProbabilityPositiveGreaterThanMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(3);
        assertEquals(0.8804549137631941, cumulativeProbability, 0.0000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        assertEquals(0.5997469440706314, poissonDistribution.normalApproximateProbability(0), 0.0000001);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.21959786650128354, probability, 0.0000001);
    }

    @Test
    public void testSampleZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        int sample = poissonDistribution.sample();
        assertEquals(0, sample);
    }

    @Test
    public void testSampleOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        int sample = poissonDistribution.sample();
        assertEquals(1, sample);
    }

    @Test
    public void testSampleGreaterThanZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertTrue(sample > 0);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        int x = poissonDistribution.inverseCumulativeProbability(0);
        assertEquals(0, x);
    }

    @Test
    public void testInverseCumulativeProbabilityGreaterThanZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int x = poissonDistribution.inverseCumulativeProbability(0.6);
        assertEquals(1, x);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int x = poissonDistribution.inverseCumulativeProbability(1);
        assertEquals(2, x);
    }

    @Test
    public void testInverseCumulativeProbabilityGreaterThanOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int x = poissonDistribution.inverseCumulativeProbability(1.2);
        assertTrue(x > 2);
    }
}