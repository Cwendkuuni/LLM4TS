import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    // Test constructor
    @Test
    public void testConstructor() {
        FDistribution f = new FDistribution(4, 6);
        assertEquals(4, f.getNumeratorDegreesOfFreedom(), 0);
        assertEquals(6, f.getDenominatorDegreesOfFreedom(), 0);
    }

    // Test density method
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(4, 6);
        double result = f.density(2.5);
        assertEquals(0.229, result, 0.001);
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(4, 6);
        double result = f.cumulativeProbability(2.5);
        assertEquals(0.908, result, 0.001);
    }

    // Test getNumericalMean method for b > 2
    @Test
    public void testGetNumericalMean_bGreaterThan2() {
        FDistribution f = new FDistribution(4, 6);
        double result = f.getNumericalMean();
        assertEquals(1.5, result, 0);
    }

    // Test getNumericalMean method for b <= 2
    @Test
    public void testGetNumericalMean_bLessThanOrEqualTo2() {
        FDistribution f = new FDistribution(4, 1.5);
        double result = f.getNumericalMean();
        assertTrue(Double.isNaN(result));
    }

    // Test getNumericalVariance method for b > 4
    @Test
    public void testGetNumericalVariance_bGreaterThan4() {
        FDistribution f = new FDistribution(5, 7);
        double result = f.getNumericalVariance();
        assertEquals(2.1, result, 0.001);
    }

    // Test getNumericalVariance method for b <= 4
    @Test
    public void testGetNumericalVariance_bLessThanOrEqualTo4() {
        FDistribution f = new FDistribution(3, 4);
        double result = f.getNumericalVariance();
        assertTrue(Double.isNaN(result));
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(4, 6);
        double result = f.getSupportLowerBound();
        assertEquals(0, result, 0);
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(4, 6);
        double result = f.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(4, 6);
        assertTrue(f.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(4, 6);
        assertFalse(f.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(4, 6);
        assertTrue(f.isSupportConnected());
    }

}