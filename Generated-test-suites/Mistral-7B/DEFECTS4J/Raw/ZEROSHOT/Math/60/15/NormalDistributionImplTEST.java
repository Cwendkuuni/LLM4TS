import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testMeanDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals(0.398942280401, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1;
        double result = dist.cumulativeProbability(p);
        double expected = 0.158730159225;
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 1.1;
        dist.cumulativeProbability(p);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1;
        double result = dist.inverseCumulativeProbability(p);
        double expected = -0.841621329374;
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 1.2;
        dist.inverseCumulativeProbability(p);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        double expected = mean + stdDev * FastMath.randomData.nextGaussian();
        assertEquals(expected, sample, 0.01);
    }
}