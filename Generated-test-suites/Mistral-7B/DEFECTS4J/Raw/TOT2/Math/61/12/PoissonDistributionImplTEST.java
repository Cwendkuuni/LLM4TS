import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.distribution.PoissonDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     */
    @Test
    public void testConstructorPositiveMean() {
        double mean = 2.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNegativeMean() {
        double mean = -2.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        double expectedProbability = 0.270466787277967; // probability of 0 for Poisson Distribution with mean = 2
        double actualProbability = poissonDistribution.probability(0);
        assertEquals(expectedProbability, actualProbability, 0.0);

        expectedProbability = 0.135335283236612; // probability of 1 for Poisson Distribution with mean = 2
        actualProbability = poissonDistribution.probability(1);
        assertEquals(expectedProbability, actualProbability, 0.0);

        expectedProbability = 0.223138793299576; // probability of 2 for Poisson Distribution with mean = 2
        actualProbability = poissonDistribution.probability(2);
        assertEquals(expectedProbability, actualProbability, 0.0);
    }

    /**
     * Test probability method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityInvalidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        poissonDistribution.probability(-1);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        double expectedCumulativeProbability = 0.0;
        double actualCumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);

        expectedCumulativeProbability = 0.135335283236612; // probability of 1 for Poisson Distribution with mean = 2
        actualCumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);

        expectedCumulativeProbability = 0.319863684667443; // probability of 2 for Poisson Distribution with mean = 2
        actualCumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);

        expectedCumulativeProbability = 0.682689335473517; // probability of 3 for Poisson Distribution with mean = 2
        actualCumulativeProbability = poissonDistribution.cumulativeProbability(3);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);
    }

    /**
     * Test cumulativeProbability method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        poissonDistribution.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with valid input.
     */
    @Test
    public void testNormalApproximateProbabilityValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        double expectedProbability = 0.268941421374194; // probability of 0 for Poisson Distribution with mean = 2 calculated using normal approximation
        double actualProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(expectedProbability, actualProbability, 0.0);

        expectedProbability = 0.135335283236612; // probability of 1 for Poisson Distribution with mean = 2
        actualProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(expectedProbability, actualProbability, 0.0);

        expectedProbability = 0.135335283236612; // probability of 1 for Poisson Distribution with mean = 2 calculated using normal approximation
        actualProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(expectedProbability, actualProbability, 0.0);

        expectedProbability = 0.154324454803326; // probability of 2 for Poisson Distribution with mean = 2 calculated using normal approximation
        actualProbability = poissonDistribution.normalApproximateProbability(2);
        assertEquals(expectedProbability, actualProbability, 0.0);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        int expectedSample = 1;
        int actualSample = poissonDistribution.sample();
        assertEquals(expectedSample, actualSample);

        expectedSample = 2;
        actualSample = poissonDistribution.sample();
        assertEquals(expectedSample, actualSample);

        expectedSample = 3;
        actualSample = poissonDistribution.sample();
        assertEquals(expectedSample, actualSample);
    }

    /**
     * Test sample method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testSampleInvalidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.0);
        poissonDistribution.sample(-1);
    }
}