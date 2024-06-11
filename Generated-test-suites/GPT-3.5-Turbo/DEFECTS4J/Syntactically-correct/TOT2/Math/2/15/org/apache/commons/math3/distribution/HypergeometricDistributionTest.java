import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class HypergeometricDistributionTest {
    
    private static HypergeometricDistribution hypergeometricDistribution;
    
    @BeforeClass
    public static void setUp() {
        hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.0, hypergeometricDistribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.277, hypergeometricDistribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(2, hypergeometricDistribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }
    
    @Test
    public void testConstructorExceptions() {
        try {
            HypergeometricDistribution invalidDistribution = new HypergeometricDistribution(10, -5, 2);
            fail("NotPositiveException expected");
        } catch (NotPositiveException e) {
            assertTrue(true);
        }
        
        try {
            HypergeometricDistribution invalidDistribution = new HypergeometricDistribution(0, 5, 2);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
        
        try {
            HypergeometricDistribution invalidDistribution = new HypergeometricDistribution(10, 5, 15);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }
    }
}