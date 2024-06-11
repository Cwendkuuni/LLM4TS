import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);
        
        normalDist.setMean(5.0);
        assertEquals(5.0, normalDist.getMean(), 0.0);
    }

    @Test
    public void testGetSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
        
        normalDist.setStandardDeviation(2.5);
        assertEquals(2.5, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, normalDist.cumulativeProbability(0.0), 0.0001);
        
        double result = normalDist.cumulativeProbability(2.0);
        assertTrue(result > 0.9772 && result < 0.9773);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1.0), 0.0);
        
        double result = normalDist.inverseCumulativeProbability(0.5);
        assertTrue(result > -0.00001 && result < 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationException(){
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(0.0);
    }
}