import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl_Constructor_ValidMean() {
        double mean = 3.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    @Test
    public void testPoissonDistributionImpl_Constructor_InvalidMean() {
        assertThrows(MathRuntimeException.class, () -> new PoissonDistributionImpl(-1.0));
    }

    @Test
    public void testProbability_ValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Math.exp(-3.0) * Math.pow(3.0, i) / Math.factorial(i);
            assertEquals(expectedProbability, probability, 1e-6);
        }
    }

    @Test
    public void testProbability_NegativeInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double probability = poissonDistribution.probability(-1);
        assertEquals(0.0, probability, 1e-6);
    }

    @Test
    public void testProbability_IntegerMaxValueInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double probability = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 1e-6);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = StatUtils.cumulativeProbability(new PoissonDistribution(3.0), i);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability_NegativeInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertThrows(IllegalArgumentException.class, () -> poissonDistribution.cumulativeProbability(-1));
    }

    @Test
    public void testCumulativeProbability_IntegerMaxValueInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbability_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = new NormalDistributionImpl(3.0, Math.sqrt(3.0)).cumulativeProbability(i);
            assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-6);
        }
    }

    @Test
    public void testNormalApproximateProbability_NegativeInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertThrows(IllegalArgumentException.class, () -> poissonDistribution.normalApproximateProbability(-1));
    }

    @Test
    public void testNormalApproximateProbability_IntegerMaxValueInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.0, normalApproximateProbability, 1e-6);
    }

    @Test
    public void testSample_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        for (int i = 0; i < 10; i++) {
            int sample = poissonDistribution.sample();
            assertEquals(3.0, poissonDistribution.probability(sample), 1e-6);
        }
    }

    @Test
    public void testSample_InvalidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertThrows(IllegalArgumentException.class, () -> poissonDistribution.sample(-1));
    }

    @Test
    public void testGetDomainLowerBound_ValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.0));
    }

    @Test
    public void testGetDomainUpperBound_ValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(1.0));
    }
}