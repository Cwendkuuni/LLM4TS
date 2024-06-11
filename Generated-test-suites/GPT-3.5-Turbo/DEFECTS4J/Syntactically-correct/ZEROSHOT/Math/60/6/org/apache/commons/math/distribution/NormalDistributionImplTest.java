import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

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
        assertEquals(0.3989422804014327, normalDistribution.density(0), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

}