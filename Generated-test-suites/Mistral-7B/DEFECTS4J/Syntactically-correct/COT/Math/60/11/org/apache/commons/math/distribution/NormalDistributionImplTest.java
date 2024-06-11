import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(1.0, -1.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double density = normalDistribution.density(0.5);
        assertEquals(0.12533141351136353, density, 1e-6);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 0.5;
        double cumulativeProbability = normalDistribution.cumulativeProbability(0.5);
        assertEquals(0.5, cumulativeProbability, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityWithLargeValue() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 100000.0;
        normalDistribution.cumulativeProbability(probability);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImplCumulativeProbabilityWithLargeValueAndException() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 1000000.0;
        normalDistribution.cumulativeProbability(probability);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = 0.5;
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(probability);
        assertEquals(0.0, inverseCumulativeProbability, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityWithOutOfRangeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = -0.1;
        normalDistribution.inverseCumulativeProbability(probability);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double sample = normalDistribution.sample();
        assertTrue(Precision.isClose(sample, 0.0, 1e-6));
    }

}