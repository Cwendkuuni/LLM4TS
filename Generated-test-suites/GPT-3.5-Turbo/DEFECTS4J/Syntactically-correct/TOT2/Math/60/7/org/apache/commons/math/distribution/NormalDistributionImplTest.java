import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.density(0);
        assertEquals(0.398942, result, 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(0);
        assertEquals(0.5, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, result, 0.000001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.sample();
        assertTrue(result >= Double.NEGATIVE_INFINITY);
        assertTrue(result <= Double.POSITIVE_INFINITY);
    }

}