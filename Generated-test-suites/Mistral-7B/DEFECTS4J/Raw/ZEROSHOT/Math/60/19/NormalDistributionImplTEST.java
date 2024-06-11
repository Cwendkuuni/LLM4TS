import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        NormalDistribution distMeanOne = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(1.0, distMeanOne.getMean(), 0.0);
        assertEquals(1.0, distMeanOne.getStandardDeviation(), 0.0);

        NormalDistribution distMeanZeroSDTwo = new NormalDistributionImpl(0.0, 2.0);
        assertEquals(0.0, distMeanZeroSDTwo.getMean(), 0.0);
        assertEquals(2.0, distMeanZeroSDTwo.getStandardDeviation(), 0.0);

        NormalDistribution distMeanTwoSDOne = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, distMeanTwoSDOne.getMean(), 0.0);
        assertEquals(1.0, distMeanTwoSDOne.getStandardDeviation(), 0.0);

        try {
            new NormalDistributionImpl(-1.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double density = dist.density(x);
            double p = dist.cumulativeProbability(x);
            double densityExpected = p < 0.5 ? -density : density;
            double e = Math.abs(density - densityExpected);
            assertTrue("Density for x=" + x + " is not correct.", e < 1e-4);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double density = dist.density(x);
            double e = Math.abs(p - 0.5 * (1.0 + Erf.erf(x / ( dist.getStandardDeviation() * FastMath.SQRT2PI))));
            assertTrue("Cumulative probability for x=" + x + " is not correct.", e < 1e-4);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double p2 = dist.cumulativeProbability(x);
            assertEquals("Inverse cumulative probability for p=" + p + " is not correct.", p, p2, 1e-4);
        }

        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertEquals("Sample value is not correct.", dist.density(x), 0.3989422804014327, 1e-4);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals("Solver absolute accuracy is not correct.", NormalDistributionImpl.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 1e-8);
    }
}