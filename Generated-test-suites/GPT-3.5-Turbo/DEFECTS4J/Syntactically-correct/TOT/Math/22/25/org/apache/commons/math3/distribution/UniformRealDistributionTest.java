import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class UniformRealDistributionTest {

    private RandomGenerator mockRNG = mock(Well19937c.class);
    
    @Test
    public void testConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertEquals(2, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(5, distribution.getSupportUpperBound(), 0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(5, 2);
    }
    
    @Test
    public void testConstructorWithInverseCumAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1, 0.001);
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testConstructorWithRng() {
        UniformRealDistribution distribution = new UniformRealDistribution(mockRNG, 0, 1, 0.001);
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testDensityInsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(1, distribution.density(0.5), 0);
    }
    
    @Test
    public void testDensityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0, distribution.density(-1), 0);
        Assert.assertEquals(0, distribution.density(2), 0);
    }
    
    @Test
    public void testCumulativeProbabilityInsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
    }
    
    @Test
    public void testCumulativeProbabilityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0, distribution.cumulativeProbability(-1), 0);
        Assert.assertEquals(1, distribution.cumulativeProbability(2), 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertEquals(3.5, distribution.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertEquals(2.08333333, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertEquals(2, distribution.getSupportLowerBound(), 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertEquals(5, distribution.getSupportUpperBound(), 0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(mockRNG, 0, 1);
        when(mockRNG.nextDouble()).thenReturn(0.75);
        Assert.assertEquals(0.25, distribution.sample(), 0);
    }
}