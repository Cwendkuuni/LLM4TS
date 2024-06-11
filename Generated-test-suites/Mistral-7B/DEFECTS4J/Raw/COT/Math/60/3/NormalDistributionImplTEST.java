import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor_ValidArguments() {
        double mean = 5.0;
        double standardDeviation = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals("Mean should be set correctly", mean, distribution.getMean(), 0.0);
        assertEquals("Standard deviation should be set correctly", standardDeviation, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructor_InvalidArgument() {
        double mean = 5.0;
        double standardDeviation = 0.0;
        new NormalDistributionImpl(mean, standardDeviation);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        for (double x = -5; x <= 5; x += 0.1) {
            double density = distribution.density(x);
            assertEquals("Density should be correct for x = " + x, Math.exp(-0.5 * Math.pow(x, 2)) / (Math.sqrt(2 * Math.PI)), density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        for (double x = -4; x <= 4; x += 0.1) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + org.apache.commons.math3.special.Erf.erf(x / Math.sqrt(2.0)));
            assertEquals("Cumulative probability should be correct for x = " + x, expected, cumulativeProbability, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_InvalidArgument() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        for (double p = 0; p <= 1; p += 0.01) {
            double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            double expected = p * 2 - 0.5 * Math.log(2 * Math.PI) - Math.log(1 - Math.pow(p, 2));
            assertEquals("Inverse cumulative probability should be correct for p = " + p, expected, inverseCumulativeProbability, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_OutOfRange() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-1);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        double mean = distribution.getMean();
        double standardDeviation = distribution.getStandardDeviation();
        double expected = mean + standardDeviation * org.apache.commons.math3.random.RandomValues.nextGaussian();
        assertEquals("Sample should be within the distribution", expected, sample, 0.01);
    }
}