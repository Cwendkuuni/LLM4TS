import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Before
    public void setUp() {
        // Initialize any resources needed for the tests
    }
    
    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(15), 0.0001);
        assertEquals(0.2857, distribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(15), 0.0001);
        assertEquals(0.2857, distribution.probability(2), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(15), 0.0001);
        assertEquals(0.7143, distribution.upperCumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(0.35, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(2, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = null;
        try {
            distribution = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
        
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 15);
    }
    
    // Add more test cases to cover additional scenarios
    
    // 
    
    // 
    // Test cases created previously
    @Test
    public void testConstructor() {
        //...
    }
    
    @Test
    public void testCumulativeProbability() {
        //...
    }
    
    // Additional test cases
    @Test
    public void testAnotherConstructor() {
        //...
    }
    
    @Test
    public void testAnotherMethod() {
        //...
    }
    // 
    
    // Add more test cases
    
    // 
}