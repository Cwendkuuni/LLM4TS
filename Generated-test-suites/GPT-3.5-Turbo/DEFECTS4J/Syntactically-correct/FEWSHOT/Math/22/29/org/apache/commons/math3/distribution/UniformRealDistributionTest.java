import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {
 
    @Test
    public void testConstructor() {
        // Test default constructor
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
      
        // Test constructor with lower and upper bounds
        distribution = new UniformRealDistribution(1, 3);
        assertEquals(1, distribution.getSupportLowerBound(), 0);
        assertEquals(3, distribution.getSupportUpperBound(), 0);
      
        // Test constructor with lower and upper bounds and inverse cumulative probability accuracy
        distribution = new UniformRealDistribution(1, 3, 0.001);
        assertEquals(1, distribution.getSupportLowerBound(), 0);
        assertEquals(3, distribution.getSupportUpperBound(), 0);
        assertEquals(0.001, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidArguments() {
        new UniformRealDistribution(3, 1);
    }
  
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
      
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(2), 0);
    }
  
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
      
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
        assertEquals(1, distribution.cumulativeProbability(2), 0);
    }
  
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(2, distribution.getNumericalMean(), 0);
    }
  
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(1.333, distribution.getNumericalVariance(), 0.001);
    }
  
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(1, distribution.getSupportLowerBound(), 0);
    }
  
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(3, distribution.getSupportUpperBound(), 0);
    }
  
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
  
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
  
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertTrue(distribution.isSupportConnected());
    }
  
    @Test
    public void testSampleWithWell19937cGenerator() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3, 0.001);
      
        double sample = distribution.sample(new Well19937c());
        assertTrue(sample >= 1);
        assertTrue(sample < 3);
    }
  
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3, 0.001);
      
        double sample = distribution.sample();
        assertTrue(sample >= 1);
        assertTrue(sample < 3);
    }
  
    @Test
    public void testSamplingConsistency() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3, 0.001);
      
        double sum = 0;
        int n = 10000;
        for (int i = 0; i < n; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 1);
            assertTrue(sample < 3);
            sum += sample;
        }
      
        double expectedMean = distribution.getNumericalMean();
        double epsilon = expectedMean * 0.01;
        assertEquals(expectedMean, sum / n, epsilon);
    }
}