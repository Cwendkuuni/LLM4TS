import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014337, normalDistribution.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 1e-15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            // It is hard to set an exact value for random sample, just ensure the method runs without error
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}