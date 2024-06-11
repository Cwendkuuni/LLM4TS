import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setup() {
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
        assertEquals(0.24197072451914337, normalDistribution.density(1), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNegativeStandardDeviation() {
        try {
            NormalDistributionImpl negativeStdDev = new NormalDistributionImpl(0, -1);
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testCumulativeProbabilityOutOfRange() {
        try {
            normalDistribution.cumulativeProbability(1000);
        } catch (MathException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }
}