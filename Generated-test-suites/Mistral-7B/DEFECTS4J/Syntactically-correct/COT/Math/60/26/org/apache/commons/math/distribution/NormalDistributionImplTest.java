import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

    @Test
    public void testConstructorNormalDistributionImplWithMeanAndStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.001);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructorNormalDistributionImplWithMeanAndStandardDeviationAndInverseCumulativeAccuracy() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0, 0.001);
        assertEquals(1.0, normalDistribution.getMean(), 0.001);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructorNormalDistributionImplWithInvalidStandardDeviation() {
        assertThrows(NotStrictlyPositiveException.class, () -> {
            new NormalDistributionImpl(1.0, 0.0);
        });
    }

    @Test
    public void testDensity() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        double density = normalDistribution.density(2.0);
        assertEquals(0.0398942161, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        double cumulativeProbability = normalDistribution.cumulativeProbability(1.5);
        assertEquals(0.066800846, cumulativeProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithMaxIterationsExceeded() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        normalDistribution.cumulativeProbability(100000.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, inverseCumulativeProbability, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        double sample = normalDistribution.sample();
        assertEquals(1.16713114, sample, 0.001);
    }
}