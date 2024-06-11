import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    private final double DELTA = 1e-9;
    
    @Before
    public void setup() {
        distribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testDensity() {
        double result = distribution.density(0);
        assertEquals(0.3989422804014327, result, DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = distribution.cumulativeProbability(0);
        assertEquals(0.5, result, DELTA);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, result, DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        double result = distribution.sample();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double result = distribution.getSolverAbsoluteAccuracy();
        assertEquals(1e-9, result, DELTA);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = distribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, result, DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = distribution.getDomainUpperBound(0.3);
        assertEquals(Double.MAX_VALUE, result, DELTA);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = distribution.getInitialDomain(0.3);
        assertEquals(-1, result, DELTA);
    }
    
}