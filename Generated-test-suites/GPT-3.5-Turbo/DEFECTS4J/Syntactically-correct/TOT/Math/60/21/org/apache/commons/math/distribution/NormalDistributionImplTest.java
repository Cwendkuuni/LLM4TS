import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        // Typical Use Case
        double density = normalDistribution.density(0);
        assertEquals(0.3989422804014327, density, 1e-9);

        // Edge Cases
        density = normalDistribution.density(Double.MAX_VALUE);
        assertEquals(0.0, density, 1e-9);

        density = normalDistribution.density(Double.MIN_VALUE);
        assertEquals(3.989422804014327E-315, density, 1e-9);

        // Error Scenario
        density = normalDistribution.density(Double.NaN);
        assertEquals(Double.NaN, density, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            // Typical Use Case
            double cumulativeProbability = normalDistribution.cumulativeProbability(0);
            assertEquals(0.5, cumulativeProbability, 1e-9);

            // Edge Cases
            cumulativeProbability = normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY);
            assertEquals(1.0, cumulativeProbability, 1e-9);

            cumulativeProbability = normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
            assertEquals(0.0, cumulativeProbability, 1e-9);

            // Error Scenario
            cumulativeProbability = normalDistribution.cumulativeProbability(Double.NaN);
            assertEquals(Double.NaN, cumulativeProbability, 1e-9);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            // Typical Use Case
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, inverseCumulativeProbability, 1e-9);

            // Edge Cases
            inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.0);
            assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 1e-9);

            inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 1e-9);

            // Error Scenario
            inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(2.0);
            assertEquals(Double.NaN, inverseCumulativeProbability, 1e-9);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            // Typical Use Case
            double sample = normalDistribution.sample();
            assertEquals(0.0, sample, 10);

            // Error Scenario
            sample = normalDistribution.sample();
            assertEquals(Double.NaN, sample, 1e-9);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}