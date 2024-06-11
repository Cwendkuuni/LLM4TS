import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normal.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(3.0);
        assertEquals(3.0, normal.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, normal.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(4.0);
        assertEquals(4.0, normal.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(1.0, 2.0);
        double result = normal.cumulativeProbability(3.0);
        assertEquals(0.841344746, result, 0.0000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(1.0, 2.0);
        double result = normal.inverseCumulativeProbability(0.5);
        assertEquals(1.0, result, 0.0001);
    }
}