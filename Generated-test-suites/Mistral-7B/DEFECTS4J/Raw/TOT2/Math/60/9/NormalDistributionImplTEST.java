import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_sd1() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        assertEquals(0.5, dist.density(0.0), 0.001);
        assertEquals(0.0401, dist.density(0.5), 0.001);
        assertEquals(0.0, dist.density(10.0), 0.001);

        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.01);
        assertEquals(0.8413, dist.cumulativeProbability(0.5), 0.01);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(100.0));

        assertEquals(1.5, dist.inverseCumulativeProbability(0.75), 0.01);
        assertEquals(-1.5, dist.inverseCumulativeProbability(0.25), 0.01);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.5));

        NormalDistribution distSampled = new NormalDistributionImpl();
        for (int i = 0; i < 100; i++) {
            double sample = distSampled.sample();
            assertEquals(0.0, dist.density(sample), 0.01);
            assertEquals(sample, dist.cumulativeProbability(sample), 0.01);
        }
    }

    @Test
    public void testNormalDistributionImpl_mean1_sd2() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);

        assertEquals(0.2113, dist.density(1.0), 0.001);
        assertEquals(0.1544, dist.density(2.0), 0.001);
        assertEquals(0.0, dist.density(15.0), 0.001);

        assertEquals(0.8413, dist.cumulativeProbability(1.0), 0.01);
        assertEquals(0.9773, dist.cumulativeProbability(2.0), 0.01);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(30.0));

        assertEquals(1.0, dist.inverseCumulativeProbability(0.16), 0.01);
        assertEquals(-1.0, dist.inverseCumulativeProbability(0.9), 0.01);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.0));

        NormalDistribution distSampled = new NormalDistributionImpl(1.0, 2.0);
        for (int i = 0; i < 100; i++) {
            double sample = distSampled.sample();
            assertEquals(1.0, dist.density(sample), 0.01);
            assertEquals(sample, dist.cumulativeProbability(sample), 0.01);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_sdNegative() {
        new NormalDistributionImpl(1.0, -2.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImpl_cumulativeProbability_largeX() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(100000.0);
    }

}