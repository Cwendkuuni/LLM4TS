import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;

public class FDistributionTest {
    
    private final double EPSILON = 1e-9;
    
    private FDistribution fDist;
    
    @Before
    public void setup() {
        fDist = new FDistribution(5, 10);
    }
    
    @Test
    public void testDensity() {
        double x = 2.0;
        double expected = 0.10760234346262031;
        double result = fDist.density(x);
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        double x = 2.0;
        double expected = 0.8903661206541744;
        double result = fDist.cumulativeProbability(x);
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expected = 5;
        double result = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expected = 10;
        double result = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        double expected = 1.25;
        double result = fDist.getNumericalMean();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double expected = 0.78125;
        double result = fDist.getNumericalVariance();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double expected = 0;
        double result = fDist.getSupportLowerBound();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double expected = Double.POSITIVE_INFINITY;
        double result = fDist.getSupportUpperBound();
        assertEquals(expected, result, EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(fDist.isSupportConnected());
    }
}