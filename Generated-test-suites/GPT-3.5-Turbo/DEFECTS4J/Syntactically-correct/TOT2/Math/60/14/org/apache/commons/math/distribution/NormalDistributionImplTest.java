import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        assertEquals(0.398942, normalDistribution.density(0), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            assertEquals(0.0, sample, 5.0); // Just checking for random value generation
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    // Add more test cases for edge cases and exception handling scenarios

}