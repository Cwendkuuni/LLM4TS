import static org.junit.Assert.*;
	import org.junit.Before;
	import org.junit.Test;
	import org.apache.commons.math3.distribution.HypergeometricDistribution;
	import org.apache.commons.math3.exception.NotPositiveException;
	import org.apache.commons.math3.exception.NotStrictlyPositiveException;
	import org.apache.commons.math3.exception.NumberIsTooLargeException;
	import org.apache.commons.math3.random.Well19937c;
	
	public class HypergeometricDistributionTest {
	
	    private static final double EPSILON = 1e-9;
	    private HypergeometricDistribution hypergeometricDistribution;
	
	    @Before
	    public void setUp() {
	        hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 100, 50, 10);
	    }
	    
	    @Test
	    public void testCumulativeProbability() {
	        assertEquals(0.9999999704,hypergeometricDistribution.cumulativeProbability(5), EPSILON);
	    }
	    
	    @Test
	    public void testProbability() {
	        assertEquals(0.03511509158, hypergeometricDistribution.probability(5), EPSILON);
	    }
	    
	    @Test 
	    public void testUpperCumulativeProbability() {
	    	assertEquals(9.36E-5, hypergeometricDistribution.upperCumulativeProbability(5), EPSILON);
	    }
	
	    @Test
	    public void testGetNumericalMean() {
	        assertEquals(5, hypergeometricDistribution.getNumericalMean(), EPSILON);
	    }
	    
	    @Test
	    public void testGetNumericalVariance() {
	        assertEquals(0.9753086419753086, hypergeometricDistribution.getNumericalVariance(), EPSILON);
	    }
	    
	    @Test
	    public void testGetSupportLowerBound() {
	        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
	    }
	    
	    @Test
	    public void testGetSupportUpperBound() {
	        assertEquals(10, hypergeometricDistribution.getSupportUpperBound());
	    }
	    
	    @Test
	    public void testIsSupportConnected() {
	        assertTrue(hypergeometricDistribution.isSupportConnected());
	    }
	    
	}