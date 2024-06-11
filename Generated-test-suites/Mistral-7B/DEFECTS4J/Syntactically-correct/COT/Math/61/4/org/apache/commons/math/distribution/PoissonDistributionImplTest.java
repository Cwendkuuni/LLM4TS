import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test constructor PoissonDistributionImpl(double p)
     * @throws MathException
     */
    @Test
    public void testConstructorPoissonDistributionImplDouble() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("mean", 1.5, poissonDistribution.getMean(), 0.001);
    }

    /**
     * Test constructor PoissonDistributionImpl(double p, double epsilon, int maxIterations)
     * @throws MathException
     */
    @Test
    public void testConstructorPoissonDistributionImplDoubleDoubleInt() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5, 0.001, 10000);
        assertEquals("mean", 1.5, poissonDistribution.getMean(), 0.001);
    }

    /**
     * Test method probability(int x)
     * @throws MathException
     */
    @Test
    public void testProbabilityInt() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("probability for x = 0", 0.223138238, poissonDistribution.probability(0), 0.0001);
        assertEquals("probability for x = 1", 0.205850144, poissonDistribution.probability(1), 0.0001);
        assertEquals("probability for x = 2", 0.162675912, poissonDistribution.probability(2), 0.0001);
        assertEquals("probability for x = 5", 0.054255376, poissonDistribution.probability(5), 0.0001);
        assertEquals("probability for x = Integer.MAX_VALUE", 0, poissonDistribution.probability(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test method cumulativeProbability(int x)
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityInt() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("cumulative probability for x = 0", 0.073438599, poissonDistribution.cumulativeProbability(0), 0.0001);
        assertEquals("cumulative probability for x = 1", 0.205850144, poissonDistribution.cumulativeProbability(1), 0.0001);
        assertEquals("cumulative probability for x = 2", 0.368038525, poissonDistribution.cumulativeProbability(2), 0.0001);
        assertEquals("cumulative probability for x = 5", 0.710786186, poissonDistribution.cumulativeProbability(5), 0.0001);
        assertEquals("cumulative probability for x = Integer.MAX_VALUE", 1, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);

        try {
            poissonDistribution.cumulativeProbability(-1);
            fail("Expected MathException");
        } catch (MathException e) {
            //expected
        }
    }

    /**
     * Test method normalApproximateProbability(int x)
     * @throws MathException
     */
    @Test
    public void testNormalApproximateProbabilityInt() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("normalApproximateProbability for x = 0", 0.223138238, poissonDistribution.normalApproximateProbability(0), 0.001);
        assertEquals("normalApproximateProbability for x = 1", 0.205850144, poissonDistribution.normalApproximateProbability(1), 0.001);
        assertEquals("normalApproximateProbability for x = 2", 0.162675912, poissonDistribution.normalApproximateProbability(2), 0.001);
        assertEquals("normalApproximateProbability for x = 5", 0.054255376, poissonDistribution.normalApproximateProbability(5), 0.001);
        assertEquals("normalApproximateProbability for x = Integer.MAX_VALUE", 0, poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE), 0.001);
    }

    /**
     * Test method sample()
     * @throws MathException
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        int sample1 = poissonDistribution.sample();
        int sample2 = poissonDistribution.sample();
        int sample3 = poissonDistribution.sample();

        assertEquals("sample1", 2, sample1);
        assertEquals("sample2", 0, sample2);
        assertEquals("sample3", 1, sample3);

        PoissonDistribution poissonDistributionSmallMean = new PoissonDistributionImpl(0.1);
        int sampleSmallMean1 = poissonDistributionSmallMean.sample();
        int sampleSmallMean2 = poissonDistributionSmallMean.sample();
        int sampleSmallMean3 = poissonDistributionSmallMean.sample();

        assertEquals("sampleSmallMean1", 1, sampleSmallMean1);
        assertEquals("sampleSmallMean2", 0, sampleSmallMean2);
        assertEquals("sampleSmallMean3", 1, sampleSmallMean3);

        PoissonDistribution poissonDistributionLargeMean = new PoissonDistributionImpl(50);
        int sampleLargeMean1 = poissonDistributionLargeMean.sample();
        int sampleLargeMean2 = poissonDistributionLargeMean.sample();
        int sampleLargeMean3 = poissonDistributionLargeMean.sample();

        assertTrue("sampleLargeMean1", sampleLargeMean1 >= 45 && sampleLargeMean1 <= 55);
        assertTrue("sampleLargeMean2", sampleLargeMean2 >= 45 && sampleLargeMean2 <= 55);
        assertTrue("sampleLargeMean3", sampleLargeMean3 >= 45 && sampleLargeMean3 <= 55);
    }

    /**
     * Test method getDomainLowerBound(double p)
     */
    @Test
    public void testGetDomainLowerBoundDouble() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("getDomainLowerBound for p = 0.0", 0, poissonDistribution.getDomainLowerBound(0.0));
        assertEquals("getDomainLowerBound for p = 0.25", 0, poissonDistribution.getDomainLowerBound(0.25));
        assertEquals("getDomainLowerBound for p = 0.5", 0, poissonDistribution.getDomainLowerBound(0.5));
        assertEquals("getDomainLowerBound for p = 0.75", 0, poissonDistribution.getDomainLowerBound(0.75));
        assertEquals("getDomainLowerBound for p = 1.0", 0, poissonDistribution.getDomainLowerBound(1.0));
    }

    /**
     * Test method getDomainUpperBound(double p)
     */
    @Test
    public void testGetDomainUpperBoundDouble() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals("getDomainUpperBound for p = 0.0", Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.0));
        assertEquals("getDomainUpperBound for p = 0.25", Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.25));
        assertEquals("getDomainUpperBound for p = 0.5", Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
        assertEquals("getDomainUpperBound for p = 0.75", Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.75));
        assertEquals("getDomainUpperBound for p = 1.0", Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(1.0));
    }
}