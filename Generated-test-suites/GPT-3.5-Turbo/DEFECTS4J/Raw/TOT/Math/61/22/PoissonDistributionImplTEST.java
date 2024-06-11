import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        //Positive Test Case
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        //Probability for X = 3
        double expectedProbability = 0.222719;
        double actualProbability = distribution.probability(3);
        assertEquals(expectedProbability, actualProbability, 0.000001);

        //Negative Test Case
        //Probability for X = -1
        actualProbability = distribution.probability(-1);
        assertEquals(0.0, actualProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        //Positive Test Case
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        //Cumulative Probability for X <= 4
        double expectedCumulativeProbability = 0.785130;
        double actualCumulativeProbability = distribution.cumulativeProbability(4);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.000001);

        //Negative Test Case
        //Cumulative Probability for X <= -1
        actualCumulativeProbability = distribution.cumulativeProbability(-1);
        assertEquals(0.0, actualCumulativeProbability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        //Positive Test Case
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4);
        //Normal Approximate Probability for X <= 4
        double expectedNormalApproximateProbability = 0.744124;
        double actualNormalApproximateProbability = distribution.normalApproximateProbability(4);
        assertEquals(expectedNormalApproximateProbability, actualNormalApproximateProbability, 0.000001);

        //Negative Test Case
        //Normal Approximate Probability for X <= -1
        actualNormalApproximateProbability = distribution.normalApproximateProbability(-1);
        assertEquals(0.0, actualNormalApproximateProbability, 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        //Sample size is 1000
        int sampleSize = 1000;
        for (int i = 0; i < sampleSize; i++) {
            int randomValue = distribution.sample();
            //Check if the random value is within domain
            assertEquals(true, randomValue >= 0 && randomValue <= Integer.MAX_VALUE);
        }
    }
}