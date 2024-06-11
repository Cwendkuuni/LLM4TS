import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, testDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl();
        testDistribution.setMean(10.0);
        assertEquals(10.0, testDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, testDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl();
        testDistribution.setStandardDeviation(3.0);
        assertEquals(3.0, testDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = testDistribution.cumulativeProbability(1.0);
        assertEquals(0.84134, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = testDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl testDistribution = new NormalDistributionImpl();
        testDistribution.setStandardDeviation(-1.0);
    }

}