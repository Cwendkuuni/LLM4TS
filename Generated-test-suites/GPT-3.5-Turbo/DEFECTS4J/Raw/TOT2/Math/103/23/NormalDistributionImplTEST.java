import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;
    
    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.001);
        } catch (MathException e) {
            fail("Exception thrown.");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.0);
        } catch (MathException e) {
            fail("Exception thrown.");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityInvalid() {
        try {
            normalDistribution.inverseCumulativeProbability(1.5);
            fail("Expected MathException not thrown.");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
}