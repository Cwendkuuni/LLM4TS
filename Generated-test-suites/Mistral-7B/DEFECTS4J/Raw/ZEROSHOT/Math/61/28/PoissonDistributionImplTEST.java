import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.102362455, poissonDistribution.probability(1), 0.0001);
        assertEquals(0.223138475, poissonDistribution.probability(2), 0.0001);
        assertEquals(0.270433283, poissonDistribution.probability(3), 0.0001);
        assertEquals(0.216506366, poissonDistribution.probability(4), 0.0001);
        assertEquals(0.154326119, poissonDistribution.probability(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDistribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.049812336, poissonDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.150326342, poissonDistribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.331193896, poissonDistribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.500000000, poissonDistribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.658762817, poissonDistribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.822105255, poissonDistribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.102362455, poissonDistribution.probability(1), 0.0001);
        assertEquals(0.10180143, poissonDistribution.normalApproximateProbability(1), 0.0001);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertEquals(2, sample, 0);
    }

    @Test
    public void testCumulativeProbabilityWithLargeMean() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(50);
        assertEquals(0.508466028, cumulativeProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithLargeNegativeMean() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(-100.0);
        poissonDistribution.cumulativeProbability(50);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithLargePositiveMean() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100000.0);
        poissonDistribution.cumulativeProbability(100000);
    }

    @Test
    public void testCumulativeProbabilityWithLargePositiveMeanAndLargeValue() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100000.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE - 1);
        assertEquals(1.0, cumulativeProbability, 0.0);
    }
}