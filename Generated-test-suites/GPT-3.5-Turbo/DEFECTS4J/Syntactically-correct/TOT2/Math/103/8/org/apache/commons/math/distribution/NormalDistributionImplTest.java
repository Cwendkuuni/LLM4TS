import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(10.0);
        assertEquals(10.0, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(5.0);
        assertEquals(5.0, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.001);
    }
    
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(-1.0);
    }
}