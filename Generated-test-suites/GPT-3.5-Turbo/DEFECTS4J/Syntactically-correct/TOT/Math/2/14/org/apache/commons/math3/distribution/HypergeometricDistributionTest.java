import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(1000, 500, 200);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(500, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(1000, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(200, distribution.getSampleSize());
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.01912693329993675, distribution.probability(80), 0.000001);
        assertEquals(0.0, distribution.probability(1000), 0.0);
        assertEquals(0.002490896506275175, distribution.probability(150), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.12729057268281827, distribution.cumulativeProbability(80), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(1000), 0.0);
        assertEquals(0.8816090403091818, distribution.cumulativeProbability(150), 0.000001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.8727094273171817, distribution.upperCumulativeProbability(80), 0.000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(1000), 0.0);
        assertEquals(0.11839095969081829, distribution.upperCumulativeProbability(150), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(100, distribution.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(25.918367346938776, distribution.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(300, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(200, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

}