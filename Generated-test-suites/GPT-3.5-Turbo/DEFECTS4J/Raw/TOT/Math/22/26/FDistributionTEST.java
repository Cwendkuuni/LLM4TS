import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.distribution.FDistribution;

import static org.junit.Assert.*;

public class FDistributionTest {
    
    private static final double EPSILON = 1e-9;
    private FDistribution fDistribution;
    
    @Before
    public void setUp() {
        fDistribution = new FDistribution(10, 20);
    }
    
    @Test
    public void testConstructorWithValidParameters() {
        assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), EPSILON);
        assertEquals(20, fDistribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDF() {
        new FDistribution(-10, 20);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDF() {
        new FDistribution(10, -20);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.0023827733864821606, fDistribution.density(0.5), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.5864609239132423, fDistribution.cumulativeProbability(0.5), EPSILON);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(20, fDistribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.25, fDistribution.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(22.916666666666668, fDistribution.getNumericalVariance(), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, fDistribution.getSupportLowerBound(), EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }
    
}