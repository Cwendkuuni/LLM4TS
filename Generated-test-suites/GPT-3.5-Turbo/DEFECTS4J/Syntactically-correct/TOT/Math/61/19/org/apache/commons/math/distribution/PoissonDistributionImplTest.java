import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poissonDistribution;

    @Before
    public void setUp() {
        double mean = 5.0;
        poissonDistribution = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testGetMean() {
        double expected = 5.0;
        double actual = poissonDistribution.getMean();
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        double mean = -5.0;
        PoissonDistributionImpl invalidPoissonDistribution = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        // Typical use case
        int x = 3;
        double expected = 0.1403738958142805;
        double actual = poissonDistribution.probability(x);
        assertEquals(expected, actual, 0.0001);

        // Edge case
        x = 0;
        expected = 0.006737946999085467;
        actual = poissonDistribution.probability(x);
        assertEquals(expected, actual, 0.0001);

        // Error scenario
        x = Integer.MAX_VALUE;
        expected = 0.0;
        actual = poissonDistribution.probability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case
        int x = 3;
        double expected = 0.2650259152973618;
        double actual = poissonDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Edge case
        x = 0;
        expected = 0.006737946999085467;
        actual = poissonDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Error scenario
        x = Integer.MAX_VALUE;
        expected = 1.0;
        actual = poissonDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        // Typical use case
        int x = 3;
        double expected = 0.3153107297227719;
        double actual = poissonDistribution.normalApproximateProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Edge case
        x = 0;
        expected = 0.040059156863817086;
        actual = poissonDistribution.normalApproximateProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Error scenario
        x = Integer.MAX_VALUE;
        double delta = 0.0001;
        double result = poissonDistribution.normalApproximateProbability(x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSample() throws MathException {
        // Typical use case
        int samples = 1000000;
        int[] countArray = new int[11]; // To hold counts from 0 to 10
        int mean = 5;
        PoissonDistributionImpl meanDistribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i < samples; i++) {
            int sample = meanDistribution.sample();
            if (sample >= 0 && sample <= 10) {
                countArray[sample]++;
            }
        }

        // Ensure the sampled distribution is close to the expected mean distribution
        double expectedMean = samples / 11.0;
        for (int i = 0; i < 11; i++) {
            double actualMean = countArray[i] / (samples / 11.0);
            assertEquals(expectedMean, actualMean, 0.1);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.2;
        int expected = 0;
        int actual = poissonDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.8;
        int expected = Integer.MAX_VALUE;
        int actual = poissonDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual);
    }

}