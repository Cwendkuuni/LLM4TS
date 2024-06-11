import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        assertEquals(0.5, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(0.5, -1.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        double density = distribution.density(0.6);
        assertEquals(0.010988468333184225, density, 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        double probability = distribution.cumulativeProbability(0.7);
        assertEquals(0.7415754768873733, probability, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        distribution.cumulativeProbability(10.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        double inverseProbability = distribution.inverseCumulativeProbability(0.25);
        assertEquals(-0.6794578138742412, inverseProbability, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        distribution.inverseCumulativeProbability(0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        distribution.inverseCumulativeProbability(1.0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        double sample = distribution.sample();
        assertEquals(0.5, sample, 0.01);
    }

    @Test
    public void testSolverAbsoluteAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.5, 1.0);
        double absoluteAccuracy = distribution.getSolverAbsoluteAccuracy();
        assertTrue(absoluteAccuracy > 0.0);
    }

}