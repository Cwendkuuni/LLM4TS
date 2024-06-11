import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    // Test constructor with positive degrees of freedom
    @Test
    public void testConstructorWithPositiveDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(4, 7);
        assertNotNull(fDist);
    }

    // Test constructor with zero numerator degrees of freedom
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(0, 5);
    }

    // Test constructor with zero denominator degrees of freedom
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 0);
    }

    // Test constructor with negative numerator degrees of freedom
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(-4, 7);
    }

    // Test constructor with negative denominator degrees of freedom
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(4, -7);
    }

    // Test density function
    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(0.04954757091433628, fDist.density(1.0), 0.0000000001);
        assertEquals(0.04046344415641103, fDist.density(2.1), 0.0000000001);
        assertEquals(0.16171483599235154, fDist.density(0.5), 0.0000000001);
    }

    // Test cumulative probability function
    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(0.24992522041960446, fDist.cumulativeProbability(1.0), 0.0000000001);
        assertEquals(0.07799902304238247, fDist.cumulativeProbability(2.1), 0.0000000001);
        assertEquals(0.09989912291983749, fDist.cumulativeProbability(0.5), 0.0000000001);
    }
    
    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(1.1666666666666667, fDist.getNumericalMean(), 0.0000000001);
    }

    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(1.9047619047619047, fDist.getNumericalVariance(), 0.0000000001);
    }

    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(0, fDist.getSupportLowerBound(), 0);
    }

    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(4, 7);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0);
    }

    // Test isSupportLowerBoundInclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(4, 7);
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(4, 7);
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(4, 7);
        assertTrue(fDist.isSupportConnected());
    }

}