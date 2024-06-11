import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, normalDist.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(3.0);
        assertEquals(3.0, normalDist.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(4.0);
        assertEquals(4.0, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, normalDist.cumulativeProbability(0.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.95996, normalDist.inverseCumulativeProbability(0.025), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationException() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(0.0);
    }

}