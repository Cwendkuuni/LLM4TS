import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor() {
        new FDistribution(0, 1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor2() {
        new FDistribution(1, -1);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(3, f.getNumeratorDegreesOfFreedom(), 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(4, f.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(0, f.density(0), 0);
        assertEquals(0.098126, f.density(1), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(0, f.cumulativeProbability(0), 0);
        assertEquals(0.091372, f.cumulativeProbability(1), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(2, f.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(16, f.getNumericalVariance(), 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(0, f.getSupportLowerBound(), 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(3, 4);
        assertTrue(f.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(3, 4);
        assertFalse(f.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(3, 4);
        assertTrue(f.isSupportConnected());
    }
}