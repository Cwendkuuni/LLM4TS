import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_Constructor_ValidParameters() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_Constructor_InvalidStandardDeviation() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 0;
        double density = dist.density(x);
        assertEquals(0.3989423, density, 0.001);

        x = 1;
        density = dist.density(x);
        assertEquals(0.3989423 / Math.sqrt(2 * Math.PI), density, 0.001);

        x = 3;
        density = dist.density(x);
        assertEquals(0.0013501, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x;
        double probability;

        x = -3;
        probability = dist.cumulativeProbability(x);
        assertEquals(0, probability, 0.001);

        x = -2;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.1548386, probability, 0.001);

        x = 0;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.5, probability, 0.001);

        x = 2;
        probability = dist.cumulativeProbability(x);
        assertEquals(0.8451614, probability, 0.001);

        x = 3;
        probability = dist.cumulativeProbability(x);
        assertEquals(1, probability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_OutOfRange() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = Double.POSITIVE_INFINITY;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p;
        double x;

        p = 0.25;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-0.6794661, x, 0.001);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0, x, 0.001);

        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.6794661, x, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = -0.1;
        dist.inverseCumulativeProbability(p);
    }
}