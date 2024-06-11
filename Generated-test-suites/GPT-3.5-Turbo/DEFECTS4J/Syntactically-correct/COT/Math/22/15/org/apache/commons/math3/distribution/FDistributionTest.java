
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FDistributionTest {
    
    private FDistribution fDistribution;
    
    @Before
    public void setup() {
        fDistribution = new FDistribution(3, 5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-1, 5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(3, -5);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testDensityNegativeX() {
        fDistribution.density(-1);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeX() {
        fDistribution.cumulativeProbability(-1);
    }
    
    @Test
    public void testDensity() {
        double density = fDistribution.density(1.5);
        assertTrue(Precision.equalsWithRelativeTolerance(density, 0.21141295306, 1e-9));
    }
    
    @Test
    public void testCumulativeProbability() {
        double probability = fDistribution.cumulativeProbability(1.5);
        assertTrue(Precision.equalsWithRelativeTolerance(probability, 0.7475074627, 1e-9));
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(numeratorDegreesOfFreedom, 3, 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(denominatorDegreesOfFreedom, 5, 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(numericalMean, 2.5, 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        assertTrue(Precision.equalsWithRelativeTolerance(numericalVariance, 10.0 / 7.0, 1e-9));
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(supportLowerBound, 0, 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(supportUpperBound, Double.POSITIVE_INFINITY, 0);
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
