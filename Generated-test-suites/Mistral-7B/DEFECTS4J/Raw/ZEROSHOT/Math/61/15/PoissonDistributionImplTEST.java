import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstruction() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        assertEquals(mean, poissonDistributionImpl.getMean(), 0.001);
        assertEquals(PoissonDistributionImpl.class, poissonDistribution.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        double mean = -2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double probability = poissonDistributionImpl.probability(0);
        assertEquals(1.0 / Math.exp(2.5), probability, 0.001);
        probability = poissonDistributionImpl.probability(1);
        assertEquals(2.5 / Math.exp(2.5), probability, 0.001);
        probability = poissonDistributionImpl.probability(5);
        assertEquals(Math.exp(-2.5) * Math.pow(2.5, 5) / Math.factorial(5), probability, 0.001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double cumulativeProbability = poissonDistributionImpl.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 0.001);
        cumulativeProbability = poissonDistributionImpl.cumulativeProbability(1);
        assertEquals(0.69146261465, cumulativeProbability, 0.001);
        cumulativeProbability = poissonDistributionImpl.cumulativeProbability(5);
        assertEquals(0.99936864748, cumulativeProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        NormalDistribution normalDistribution = new NormalDistributionImpl(2.5, Math.sqrt(2.5));
        double normalApproximateProbability = poissonDistributionImpl.normalApproximateProbability(0);
        assertEquals(normalDistribution.cumulativeProbability(0), normalApproximateProbability, 0.001);
        normalApproximateProbability = poissonDistributionImpl.normalApproximateProbability(1);
        assertEquals(normalDistribution.cumulativeProbability(1), normalApproximateProbability, 0.001);
        normalApproximateProbability = poissonDistributionImpl.normalApproximateProbability(5);
        assertEquals(normalDistribution.cumulativeProbability(5), normalApproximateProbability, 0.001);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        int sample = poissonDistributionImpl.sample();
        assertEquals(2, sample);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double p = 0.95;
        int inverseCumulativeProbability = poissonDistributionImpl.inverseCumulativeProbability(p);
        assertEquals(2, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilitySmallSampleSize() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(0.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double p = 0.9;
        int inverseCumulativeProbability = poissonDistributionImpl.inverseCumulativeProbability(p);
        assertEquals(0, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilityLargeSampleSize() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double p = 0.999;
        int inverseCumulativeProbability = poissonDistributionImpl.inverseCumulativeProbability(p);
        assertEquals(10, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilityApproximation() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDistribution;
        double p = 0.95;
        int inverseCumulativeProbability = poissonDistributionImpl.inverseCumulativeProbability(p);
        double inverseCumulativeProbabilityApproximation = poissonDistribution.inverseCumulativeProbability(p);
        assertEquals(inverseCumulativeProbability, (int) inverseCumulativeProbabilityApproximation, 0);
    }
}