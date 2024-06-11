import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class UniformRealDistributionTest {
    
    private UniformRealDistribution uniformRealDistribution;
    
    @Before
    public void setUp() {
        uniformRealDistribution = new UniformRealDistribution();
    }
    
    @After
    public void tearDown() {
        uniformRealDistribution = null;
    }
    
    @Test
    public void testDensity_atLowerBound() {
        double x = 0.0;
        double density = uniformRealDistribution.density(x);
        assertEquals(1.0, density, 0.00001);
    }
    
    @Test
    public void testDensity_atUpperBound() {
        double x = 1.0;
        double density = uniformRealDistribution.density(x);
        assertEquals(1.0, density, 0.00001);
    }
    
    @Test
    public void testDensity_insideBounds() {
        double x = 0.5;
        double density = uniformRealDistribution.density(x);
        assertEquals(1.0, density, 0.00001);
    }
    
    @Test
    public void testDensity_outOfBounds() {
        double x = 1.5;
        double density = uniformRealDistribution.density(x);
        assertEquals(0.0, density, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability_atLowerBound() {
        double x = 0.0;
        double cumulativeProbability = uniformRealDistribution.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability_atUpperBound() {
        double x = 1.0;
        double cumulativeProbability = uniformRealDistribution.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability_insideBounds() {
        double x = 0.5;
        double cumulativeProbability = uniformRealDistribution.cumulativeProbability(x);
        assertEquals(0.5, cumulativeProbability, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability_outOfBounds() {
        double x = 1.5;
        double cumulativeProbability = uniformRealDistribution.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        double mean = uniformRealDistribution.getNumericalMean();
        assertEquals(0.5, mean, 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double variance = uniformRealDistribution.getNumericalVariance();
        assertEquals(0.083333, variance, 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double lowerBound = uniformRealDistribution.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double upperBound = uniformRealDistribution.getSupportUpperBound();
        assertEquals(1.0, upperBound, 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean inclusive = uniformRealDistribution.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean inclusive = uniformRealDistribution.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean connected = uniformRealDistribution.isSupportConnected();
        assertTrue(connected);
    }
    
    @Test
    public void testSample() {
        double sample = uniformRealDistribution.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withInvalidBounds() {
        double lowerBound = 3.2;
        double upperBound = 2.8;
        UniformRealDistribution distribution = new UniformRealDistribution(lowerBound, upperBound);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withInvalidBounds_mustThrowException() {
        double lowerBound = 0.5;
        double upperBound = 0.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lowerBound, upperBound);
    }
}