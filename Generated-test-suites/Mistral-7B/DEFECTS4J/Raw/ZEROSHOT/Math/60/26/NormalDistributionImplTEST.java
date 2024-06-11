import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        // Test default constructor
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        // Test constructor with mean and standard deviation
        NormalDistributionImpl dist1 = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, dist1.getMean(), 0.0);
        assertEquals(2.5, dist1.getStandardDeviation(), 0.0);

        // Test constructor with mean, standard deviation and inverse cumulative accuracy
        NormalDistributionImpl dist2 = new NormalDistributionImpl(2.0, 3.5, 0.01);
        assertEquals(2.0, dist2.getMean(), 0.0);
        assertEquals(3.5, dist2.getStandardDeviation(), 0.0);
        assertEquals(0.01, dist2.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testNormalDistributionImplInvalidArguments() {
        try {
            new NormalDistributionImpl(-1.0, 2.5);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(0.0, -2.5);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(Double.NEGATIVE_INFINITY, 2.5);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(2.5, Double.NEGATIVE_INFINITY);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImplMethods() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.0, 3.5);

        // Test getMean()
        assertEquals(2.0, dist.getMean(), 0.0);

        // Test getStandardDeviation()
        assertEquals(3.5, dist.getStandardDeviation(), 0.0);

        // Test density()
        double x = 2.5;
        double density = dist.density(x);
        double sqrt2pi = FastMath.sqrt(2 * FastMath.PI);
        double expectedDensity = (FastMath.exp(-0.5 * (Math.pow(x - dist.getMean(), 2) / (FastMath.pow(dist.getStandardDeviation(), 2)))) / (dist.getStandardDeviation() * sqrt2pi));
        assertEquals(expectedDensity, density, 0.001);

        // Test cumulativeProbability()
        double p = 0.1;
        double cdf = dist.cumulativeProbability(x);
        double erf = 0.5 * (1.0 + Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * FastMath.sqrt(2.0))));
        assertEquals(erf, cdf, 0.001);

        // Test inverseCumulativeProbability()
        double inverseCdf = dist.inverseCumulativeProbability(p);
        double z = Erf.erfInverse(p);
        double zScore = (z * dist.getStandardDeviation()) + dist.getMean();
        assertEquals(zScore, inverseCdf, 0.001);

        // Test sample()
        double sample = dist.sample();
        StandardDeviation stdDev = new StandardDeviation();
        double[] data = new double[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = dist.sample();
        }
        double meanSample = stdDev.evaluate(data);
        assertEquals(dist.getMean(), meanSample, 0.01);
    }

    @Test(expected = NoSuchElementException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.0, 3.5);
        dist.inverseCumulativeProbability(1.2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInverseCumulativeProbabilityAccuracy() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.0, 3.5);
        dist.inverseCumulativeProbability(0.0);
    }
}