import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.3989, distribution.density(0), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, distribution.inverseCumulativeProbability(0), 0.0001);
    }
    
    @Test
    public void testSample() throws MathException {
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, distribution.getInitialDomain(0.5), 0.0001);
    }
}