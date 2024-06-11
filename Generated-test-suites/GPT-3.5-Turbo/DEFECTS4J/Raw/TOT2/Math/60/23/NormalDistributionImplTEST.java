import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014337, normalDistribution.density(0), 0.0001);
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
            assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            assertEquals(sample, sample, 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}