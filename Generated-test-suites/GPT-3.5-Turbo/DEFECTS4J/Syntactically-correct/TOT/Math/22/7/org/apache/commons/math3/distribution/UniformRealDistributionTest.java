import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    private UniformRealDistribution distribution;
    private static final double DELTA = 1e-9; // Delta value for comparing doubles
    
    @Before
    public void setup() {
        distribution = new UniformRealDistribution(0, 1);
    }
    
    @Test
    public void testDensity() {
        double x = 0.5;
        double expectedDensity = 1;
        double actualDensity = distribution.density(x);
        Assert.assertEquals(expectedDensity, actualDensity, DELTA);
        
        x = 2;
        expectedDensity = 0;
        actualDensity = distribution.density(x);
        Assert.assertEquals(expectedDensity, actualDensity, DELTA);
    }
    
    @Test
    public void testCumulativeProbability() {
        double x = 0.5;
        double expectedCumulativeProbability = 0.5;
        double actualCumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, DELTA);
        
        x = 2;
        expectedCumulativeProbability = 1;
        actualCumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, DELTA);
    }
    
    @Test
    public void testGetNumericalMean() {
        double expectedMean = 0.5;
        double actualMean = distribution.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, DELTA);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = 0.08333333333333334;
        double actualVariance = distribution.getNumericalVariance();
        Assert.assertEquals(expectedVariance, actualVariance, DELTA);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double expectedLowerBound = 0;
        double actualLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(expectedLowerBound, actualLowerBound, DELTA);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double expectedUpperBound = 1;
        double actualUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(expectedUpperBound, actualUpperBound, DELTA);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
    
    @Test
    public void testSample() {
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample < 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        UniformRealDistribution invalidDistribution = new UniformRealDistribution(1, 0);
    }

}