import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(10, 5, 7);
        } catch (NotPositiveException e) {
            fail("NotPositiveException should not be thrown");
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        } catch (NumberIsTooLargeException e) {
            fail("NumberIsTooLargeException should not be thrown");
        }
    }
    
    @Test(expected=NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, -5, 7);
    }
    
    @Test(expected=NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, -7);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(0, 5, 7);
    }
    
    @Test(expected=NumberIsTooLargeException.class)
    public void testConstructorWithSuccessGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 15, 7);
    }
    
    @Test(expected=NumberIsTooLargeException.class)
    public void testConstructorWithSampleGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 17);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        double result = distribution.cumulativeProbability(3);
        assertEquals(0.5714285714285714, result, 0);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        int result = distribution.getNumberOfSuccesses();
        assertEquals(5, result);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        int result = distribution.getPopulationSize();
        assertEquals(10, result);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        int result = distribution.getSampleSize();
        assertEquals(7, result);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        double result = distribution.probability(3);
        assertEquals(0.050505050505050504, result, 0);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        double result = distribution.upperCumulativeProbability(3);
        assertEquals(0.9109124579124579, result, 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        double result = distribution.getNumericalMean();
        assertEquals(3.5, result, 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        double result = distribution.getNumericalVariance();
        assertEquals(1.2280701754385965, result, 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        int result = distribution.getSupportLowerBound();
        assertEquals(2, result);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        int result = distribution.getSupportUpperBound();
        assertEquals(5, result);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 7);
        boolean result = distribution.isSupportConnected();
        assertEquals(true, result);
    }
}