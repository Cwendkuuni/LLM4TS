import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(-1, 2, 3);
            fail("NotPositiveException was not thrown");
        } catch (NotPositiveException e) {
            // exception expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, -2, 3);
            fail("NotPositiveException was not thrown");
        } catch (NotPositiveException e) {
            // exception expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, -3);
            fail("NotPositiveException was not thrown");
        } catch (NotPositiveException e) {
            // exception expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(5, 10, 3);
            fail("NumberIsTooLargeException was not thrown");
        } catch (NumberIsTooLargeException e) {
            // exception expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 15);
            fail("NumberIsTooLargeException was not thrown");
        } catch (NumberIsTooLargeException e) {
            // exception expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(3, dist.getSampleSize());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        
        assertEquals(0.0757575, dist.cumulativeProbability(1), 0.0001);
        
        assertEquals(0.3647177, dist.cumulativeProbability(2), 0.0001);
        
        assertEquals(0.8787878, dist.cumulativeProbability(3), 0.0001);
        
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.125, dist.probability(-1), 0.0001);
        
        assertEquals(0.125, dist.probability(0), 0.0001);
        
        assertEquals(0.3787879, dist.probability(1), 0.0001);
        
        assertEquals(0.4545455, dist.probability(2), 0.0001);
        
        assertEquals(0.2272727, dist.probability(3), 0.0001);
        
        assertEquals(0.0151515, dist.probability(4), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        
        assertEquals(0.9242424, dist.upperCumulativeProbability(0), 0.0001);
        
        assertEquals(0.1515152, dist.upperCumulativeProbability(1), 0.0001);
        
        assertEquals(0.0909091, dist.upperCumulativeProbability(2), 0.0001);
        
        assertEquals(0.0060606, dist.upperCumulativeProbability(3), 0.0001);
        
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.375, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertTrue(dist.isSupportConnected());
    }
}