import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;
    
    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.24197, normalDistribution.density(0.1), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(-1.28155, normalDistribution.inverseCumulativeProbability(0.1), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
        } catch (MathException e) {
            fail("Exception thrown when sampling");
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.1), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.9), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.1), 0.0001);
    }
}