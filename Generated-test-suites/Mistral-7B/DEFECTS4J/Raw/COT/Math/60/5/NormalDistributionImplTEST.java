import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStdDev() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        assertEquals(0.5, normalDist.getMean(), 0.01);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.01);
    }

    @Test
    public void testNormalDistributionImplConstructorWithMeanStdDevAndAccuracy() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0, 0.001);
        assertEquals(0.5, normalDist.getMean(), 0.01);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.01);
        assertEquals(0.001, normalDist.getSolverAbsoluteAccuracy(), 0.0001);
    }

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndNoStdDev() {
        try {
            new NormalDistributionImpl(0.5, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double x = 0.5;
        double density = normalDist.density(x);
        double expectedDensity = 0.39894228040143271;
        assertEquals(expectedDensity, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double x = 1.5;
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = normalDist.cumulativeProbability(x);
        } catch (MathException e) {
            fail("Expected value for x = " + x);
        }
        assertEquals(0.04588461913756455, cumulativeProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double x = Double.NEGATIVE_INFINITY;
        normalDist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double p = 0.75;
        double inverseCumulativeProbability = 0.0;
        try {
            inverseCumulativeProbability = normalDist.inverseCumulativeProbability(p);
        } catch (MathException e) {
            fail("Expected value for p = " + p);
        }
        double expectedInverseCumulativeProbability = 1.1611391635827191;
        assertEquals(Precision.round(expectedInverseCumulativeProbability, 7), inverseCumulativeProbability, 0.01);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double p = 1.1;
        normalDist.inverseCumulativeProbability(p);
    }

    @Test
    public void testSample() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double sample = 0.0;
        try {
            sample = normalDist.sample();
        } catch (MathException e) {
            fail("Expected valid sample");
        }
        assertTrue(sample > -3.0 && sample < 3.0);
    }

}