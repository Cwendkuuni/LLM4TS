
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-6;

    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        assertEquals(0.0, normalDistribution.getMean(), DELTA);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testConstructorWithMeanAndStdDev() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(3.0, normalDistribution.getMean(), DELTA);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(4.5);
        assertEquals(4.5, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(3.5);
        assertEquals(3.5, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(4.0);
        assertEquals(4.0, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double expected = 0.5;
        double actual = normalDistribution.cumulativeProbability(0.0);
        assertEquals(expected, actual, DELTA);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithPZero() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        double p = 0.0;
        normalDistribution.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithPOne() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        double p = 1.0;
        normalDistribution.inverseCumulativeProbability(p);
    }

    @Test
    public void testInverseCumulativeProbabilityWithValidP() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.7;
        double expected = 0.2723881;
        double actual = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanLessThanZero() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationLessThanOrEqualZero() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbabilityWithExtremeValues() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double xMin = -20.0;
        double xMax = 20.0;
        double pMin = 0.0;
        double pMax = 1.0;

        for (double x = xMin; x <= xMax; x += 0.1) {
            double actual = normalDistribution.cumulativeProbability(x);
            assertEquals(pMin + 0.001, actual, DELTA); // For xMin, we expect close to 0
            if (x >= 0) {
                pMin += 0.01;
            }
        }

        for (double p = pMin; p <= pMax; p += 0.01) {
            double x = normalDistribution.inverseCumulativeProbability(p);
            assertEquals(x, p + 0.001, DELTA);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidP() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        double p = -0.1;
        normalDistribution.inverseCumulativeProbability(p);

        p = 1.1;
        normalDistribution.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithFailureToConverge() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        double p = 0.51;
        normalDistribution.inverseCumulativeProbability(p);
    }
}
