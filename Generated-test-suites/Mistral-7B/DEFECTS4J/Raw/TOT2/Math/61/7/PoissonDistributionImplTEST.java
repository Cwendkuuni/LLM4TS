import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean());
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -1.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        fail("Expected MathRuntimeException with message 'NotPositivePoissonMean'");
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.2401, poissonDistribution.probability(0), 0.001);
    }

    @Test
    public void testProbabilityValidValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.3679, poissonDistribution.probability(1), 0.001);
    }

    @Test
    public void testProbabilityLargeValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.000161, poissonDistribution.probability(20), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityNegativeInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.cumulativeProbability(-1);
        fail("Expected MathRuntimeException with message 'ArgumentOutOfRangeException'");
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.3679, poissonDistribution.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilityValidValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.6878, poissonDistribution.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testCumulativeProbabilityLargeValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.99973, poissonDistribution.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testSampleValidMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * 1.0);
    }

    @Test
    public void testSampleLargeMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(50.0);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * 50.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSampleNegativeMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(-1.0);
        poissonDistribution.sample();
        fail("Expected MathRuntimeException with message 'NotPositivePoissonMean'");
    }
}