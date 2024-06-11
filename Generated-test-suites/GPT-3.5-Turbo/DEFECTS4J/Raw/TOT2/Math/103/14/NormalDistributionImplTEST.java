import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, normalDist.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(5.0);
        assertEquals(5.0, normalDist.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(2.0, 1.5);
        assertEquals(1.5, normalDist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(2.5);
        assertEquals(2.5, normalDist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0.0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(-1.0);
    }  
}