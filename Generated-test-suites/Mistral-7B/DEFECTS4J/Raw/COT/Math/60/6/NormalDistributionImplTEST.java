import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDistribution.getMean(), 0.001);
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double result = normalDistribution.density(0.5);
        assertEquals(0.15430806348152744, result, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double result = normalDistribution.cumulativeProbability(0.5);
        assertEquals(0.6826894621366198, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.cumulativeProbability(100);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double result = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.4082482904638631, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityInvalidProbability() throws MathException, NotStrictlyPositiveException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double result = normalDistribution.sample();
        assertEquals(0, result, 0.01); // Approximate testing due to randomness
    }

    @Test
    public void testNormalDistributionImplSampleMultipleTimes() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double[] results = new double[1000];
        for (int i = 0; i < 1000; i++) {
            results[i] = normalDistribution.sample();
        }
        double mean = 0;
        for (double result : results) {
            mean += result;
        }
        mean /= 1000;
        assertEquals(0, mean, 0.01); // Approximate testing due to randomness
    }
}