import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FDistributionTest {
    
    private FDistribution fDistribution;
    
    @Before
    public void setUp() {
        fDistribution = new FDistribution(5, 10);
    }
    
    @Test
    public void testDensity() {
        double density = fDistribution.density(2.5);
        assertEquals(0.256138839729691, density, 1e-9);
    }
    
    @Test
    public void testDensityWithZeroValue() {
        double density = fDistribution.density(0);
        assertEquals(0, density, 1e-9);
    }
    
    @Test
    public void testDensityWithNegativeValue() {
        double density = fDistribution.density(-2.5);
        assertEquals(0, density, 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = fDistribution.cumulativeProbability(2.5);
        assertEquals(0.9322115213880755, cumulativeProbability, 1e-9);
    }
    
    @Test
    public void testCumulativeProbabilityWithZeroValue() {
        double cumulativeProbability = fDistribution.cumulativeProbability(0);
        assertEquals(0, cumulativeProbability, 1e-9);
    }
    
    @Test
    public void testCumulativeProbabilityWithNegativeValue() {
        double cumulativeProbability = fDistribution.cumulativeProbability(-2.5);
        assertEquals(0, cumulativeProbability, 1e-9);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numeratorDegreesOfFreedom, 1e-9);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denominatorDegreesOfFreedom, 1e-9);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 1e-9);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 1e-9);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(isSupportLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        assertFalse(isSupportUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = fDistribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}