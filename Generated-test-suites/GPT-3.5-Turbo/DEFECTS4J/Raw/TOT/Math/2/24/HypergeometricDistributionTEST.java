import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    private static final double EPSILON = 1e-9;
    
    private HypergeometricDistribution distribution;
    
    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(10, 5, 3);
    }
    
    @Test
    public void testConstructor_ValidArguments() {
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        distribution = new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        distribution = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        distribution = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        distribution = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.375, distribution.getNumericalVariance(), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testProbability_ValidArgument() {
        assertEquals(0.357142857, distribution.probability(0), EPSILON);
        assertEquals(0.428571429, distribution.probability(1), EPSILON);
        assertEquals(0.214285714, distribution.probability(2), EPSILON);
        assertEquals(0.0, distribution.probability(3), EPSILON);
    }
    
    @Test
    public void testProbability_OutOfRangeArgument() {
        assertEquals(0.0, distribution.probability(-1), EPSILON);
        assertEquals(0.0, distribution.probability(4), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability_ValidArgument() {
        assertEquals(0.357142857, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.785714286, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(3), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability_OutOfRangeArgument() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(4), EPSILON);
    }
    
    @Test
    public void testUpperCumulativeProbability_ValidArgument() {
        assertEquals(1.0, distribution.upperCumulativeProbability(0), EPSILON);
        assertEquals(0.642857143, distribution.upperCumulativeProbability(1), EPSILON);
        assertEquals(0.214285714, distribution.upperCumulativeProbability(2), EPSILON);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), EPSILON);
    }
    
    @Test
    public void testUpperCumulativeProbability_OutOfRangeArgument() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), EPSILON);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), EPSILON);
    }
}