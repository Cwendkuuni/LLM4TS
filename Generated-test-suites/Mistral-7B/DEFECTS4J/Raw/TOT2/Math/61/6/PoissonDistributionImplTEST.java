import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidPositiveMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithInvalidNegativeMean() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testProbabilityMethodWithValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        assertEquals(0.123456789, probability, 0.0000001);
    }

    @Test
    public void testProbabilityMethodWithZeroInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(0);
        assertEquals(0.223103856, probability, 0.0000001);
    }

    @Test
    public void testProbabilityMethodWithMaxInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityMethodWithNegativeInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.probability(-1);
    }

    @Test
    public void testCumulativeProbabilityMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.367978555, cumulativeProbability, 0.0000001);
    }

    @Test
    public void testCumulativeProbabilityMethodWithMaxInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 0.0);
    }

    @Test
    public void testCumulativeProbabilityMethodWithNegativeInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(-1);
        assertEquals(0.0, cumulativeProbability, 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.123456789, normalApproximateProbability, 0.0000001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithMaxInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(1.0, normalApproximateProbability, 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMethodWithOutOfBoundsInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(Integer.MIN_VALUE);
    }

    @Test(expected = MathException.class)
    public void testSampleMethodWithOutOfBoundsInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.sample(-1);
    }

    @Test
    public void testSampleMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testSampleMethodWithMeanAsBoundary() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertFalse(sample == 2);
        assertTrue(sample <= 2);
    }

    @Test
    public void testSampleMethodWithMaxIterationsAndEpsilon() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5, 1.0E-6, 100000);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}