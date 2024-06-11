import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(10, 2);
        assertEquals(10, normal.getMean(), 0.001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(15);
        assertEquals(15, normal.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(10, 2);
        assertEquals(2, normal.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(3);
        assertEquals(3, normal.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, normal.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationException() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(0);
    }

}