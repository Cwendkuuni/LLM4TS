import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(3.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(0.3085375387259869, distribution.cumulativeProbability(2.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(2.0, distribution.inverseCumulativeProbability(0.3085375387259869), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationException() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }
}