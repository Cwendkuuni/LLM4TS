import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testConstructors() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        NormalDistributionImpl distNegMeanAndNegSD = new NormalDistributionImpl(-1.5, -2.5);
        assertEquals(-1.5, distNegMeanAndNegSD.getMean(), 0.0);
        assertEquals(-2.5, distNegMeanAndNegSD.getStandardDeviation(), 0.0);

        try {
            NormalDistributionImpl distNegSD = new NormalDistributionImpl(0.0, -1.5);
            fail("Expected MathIllegalArgumentException with negative standard deviation.");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        double eps = 1e-5;
        for (double x = -3.0; x <= 3.0; x += 0.1) {
            double density = dist.density(x);
            double expected = 1.0 / (SQRT2PI * dist.getStandardDeviation());
            expected *= Math.exp(-0.5 * Math.pow(x / dist.getStandardDeviation(), 2.0));

            double diff = Math.abs(density - expected);
            assertTrue("Density test for x = " + x, diff <= eps);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        for (double x = -3.0; x <= 3.0; x += 0.1) {
            double cumProb = dist.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * SQRT2PI)));
            double diff = Math.abs(cumProb - expected);
            assertTrue("CDF test for x = " + x, diff <= 1e-6);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(-10);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException, NoSuchElementException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        double eps = 1e-6;
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = dist.getMean() + p * dist.getStandardDeviation();
            double diff = Math.abs(x - expected);
            assertTrue("Inverse CDF test for p = " + p, diff <= eps);
        }
    }

    @Test
    public void testInverseCumulativeProbabilityOutOfRange() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected NoSuchElementException with p > 1.");
        } catch (NoSuchElementException e) {
            // expected
        }

        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected NoSuchElementException with p < 0.");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            double mean = dist.getMean();
            double stdDev = dist.getStandardDeviation();
            double expected = mean + stdDev * FastMath.random();
            double diff = Math.abs(sample - expected);
            assertTrue("Sample test. Sample = " + sample + ", expected = " + expected, diff <= 1e-6);
        }
    }
}