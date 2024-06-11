
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        double mean = 3.5;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);
        assertEquals(mean, poisson.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroMean() {
        double mean = 0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -3.5;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        double mean = 2.0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);

        assertEquals(0.2706705664732254, poisson.probability(0), 0.0001);
        assertEquals(0.1804470443154836, poisson.probability(1), 0.0001);
        assertEquals(0.0902235221577418, poisson.probability(2), 0.0001);
        assertEquals(0.03608940886309671, poisson.probability(3), 0.0001);
        assertEquals(0.012029802954365571, poisson.probability(4), 0.0001);
        assertEquals(0.0034370865583901637, poisson.probability(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        double mean = 2.0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);

        assertEquals(0.4060058511942291, poisson.cumulativeProbability(0), 0.0001);
        assertEquals(0.6766764161830635, poisson.cumulativeProbability(1), 0.0001);
        assertEquals(0.857123460498547, poisson.cumulativeProbability(2), 0.0001);
        assertEquals(0.947213595499958, poisson.cumulativeProbability(3), 0.0001);
        assertEquals(0.9820625371448877, poisson.cumulativeProbability(4), 0.0001);
        assertEquals(0.9954997299348029, poisson.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        double mean = 2.0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);

        assertEquals(0.4012936743170763, poisson.normalApproximateProbability(0), 0.0001);
        assertEquals(0.6914624612740131, poisson.normalApproximateProbability(1), 0.0001);
        assertEquals(0.8750482517513652, poisson.normalApproximateProbability(2), 0.0001);
        assertEquals(0.9544997361036416, poisson.normalApproximateProbability(3), 0.0001);
        assertEquals(0.9840770935726944, poisson.normalApproximateProbability(4), 0.0001);
        assertEquals(0.9953524380471521, poisson.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSampleInRange() {
        double mean = 2.0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);

        for (int i = 0; i < 1000; i++) {
            int sample = poisson.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testSampleDistribution() {
        double mean = 2.0;
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(mean);

        int[] counts = new int[101];
        for (int i = 0; i < 1000; i++) {
            int sample = poisson.sample();
            counts[sample]++;
        }

        for (int i = 0; i < counts.length; i++) {
            double expectedProb = poisson.probability(i) * 1000;
            assertEquals(expectedProb, counts[i], 50);
        }
    }

}
