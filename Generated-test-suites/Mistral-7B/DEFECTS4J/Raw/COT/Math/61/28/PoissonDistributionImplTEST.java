import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.01);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.2401, poissonDistribution.probability(0), 0.001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.36787944114144715e-05, poissonDistribution.probability(3), 0.0001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testCumulativeProbabilityMiddleValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(5);
        double expected = Gamma.regularizedGammaP(5.5, 1.0);
        assertEquals(expected, cumulativeProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityWithSmallMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        double probability = poissonDistribution.normalApproximateProbability(0);
        double expected = 0.398942280401;
        assertEquals(expected, probability, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityWithLargeMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(100.0);
        double probability = poissonDistribution.normalApproximateProbability(50);
        double expected = 0.544364798476;
        assertEquals(expected, probability, 0.001);
    }

    @Test
    public void testSampleWithSmallMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        int sample = poissonDistribution.sample();
        int expected = 0;
        assertEquals(expected, sample);
    }

    @Test
    public void testSampleWithLargeMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(100.0);
        int sample = poissonDistribution.sample();
        int expected = 50;
        assertTrue(sample >= expected && sample <= expected * 1.1);
    }
}