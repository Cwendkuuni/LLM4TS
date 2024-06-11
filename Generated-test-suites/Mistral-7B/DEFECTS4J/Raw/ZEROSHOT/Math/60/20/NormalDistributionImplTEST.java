import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        NormalDistribution distZeroOne = new NormalDistributionImpl();
        assertEquals(0.0, distZeroOne.getMean(), 0.0);
        assertEquals(1.0, distZeroOne.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStdDev() {
        new NormalDistributionImpl(1.0, -1.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        double z = 0.0;
        double expectedDensity = 0.39894228040143271;
        assertEquals(expectedDensity, dist.density(z), 1e-12);
        z = 2.0;
        expectedDensity = 0.12046512642346634;
        assertEquals(expectedDensity, dist.density(z), 1e-12);
        z = 5.0;
        expectedDensity = 2.955056441509245e-15;
        assertEquals(expectedDensity, dist.density(z), 1e-12);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        double x = 0.0;
        double p = dist.cumulativeProbability(x);
        double expectedP = 0.5;
        assertEquals(expectedP, p, 1e-12);
        x = 2.0;
        p = dist.cumulativeProbability(x);
        expectedP = 0.9772498680576767;
        assertEquals(expectedP, p, 1e-12);
        x = 5.0;
        p = dist.cumulativeProbability(x);
        expectedP = 0.999999999835682;
        assertEquals(expectedP, p, 1e-12);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        double x = -10.0;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        double expectedX = 0.0;
        assertEquals(expectedX, x, 1e-12);

        p = 0.9;
        x = dist.inverseCumulativeProbability(p);
        expectedX = 1.282107551521389;
        assertEquals(expectedX, x, 1e-12);

        p = 0.01;
        x = dist.inverseCumulativeProbability(p);
        expectedX = -1.282107551521389;
        assertEquals(expectedX, x, 1e-12);

        p = 0.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x);

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);

        p = 0.25;
        x = dist.inverseCumulativeProbability(p);
        double expectedX2 = 0.5641895835477563;
        assertEquals(expectedX2, x, 1e-12);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityWhenNotPositive() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        dist.inverseCumulativeProbability(-1.0);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        dist.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        double x = dist.sample();
        assertEquals(1.0, x, 1e-12);
    }

    @Test
    public void testNormalDistributionImplSampleMultipleTimes() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        int numSamples = 1000;
        double sum = 0.0;
        for (int i = 0; i < numSamples; i++) {
            double x = dist.sample();
            sum += x;
        }
        double expectedMean = 1.0;
        assertEquals(expectedMean, sum / numSamples, 1e-12);
    }
}