import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution hypergeometric;
    
    @Before
    public void setUp() {
        hypergeometric = new HypergeometricDistribution(100, 50, 30);
    }
    
    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(-1, 50, 30);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.POPULATION_SIZE, e.getSpecifier());
            assertEquals(-1, e.getArgument());
        }
        
        try {
            new HypergeometricDistribution(100, -1, 30);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SUCCESSES, e.getSpecifier());
            assertEquals(-1, e.getArgument());
        }
        
        try {
            new HypergeometricDistribution(100, 50, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SAMPLES, e.getSpecifier());
            assertEquals(-1, e.getArgument());
        }
        
        try {
            new HypergeometricDistribution(100, 150, 200);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, e.getSpecifier());
            assertEquals(150, e.getArgument());
            assertEquals(100, e.getFormatArguments()[0]);
            assertTrue(e.getMax() > 0);
            assertTrue(e.getMax() > e.getMin());
        }
        
        try {
            new HypergeometricDistribution(100, 50, 200);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, e.getSpecifier());
            assertEquals(200, e.getArgument());
            assertEquals(100, e.getFormatArguments()[0]);
            assertTrue(e.getMax() > 0);
            assertTrue(e.getMax() > e.getMin());
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.75, hypergeometric.cumulativeProbability(20), 1e-10);
        assertEquals(1.0, hypergeometric.cumulativeProbability(30), 1e-10);
        assertEquals(0.0, hypergeometric.cumulativeProbability(-1), 1e-10);
        assertEquals(1.0, hypergeometric.cumulativeProbability(31), 1e-10);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.0, hypergeometric.probability(-1), 1e-10);
        assertEquals(0.0, hypergeometric.probability(31), 1e-10);
        assertEquals(0.0, hypergeometric.probability(20), 1e-10);
        assertEquals(0.43945068660927325, hypergeometric.probability(25), 1e-10);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(-1), 1e-10);
        assertEquals(0.25, hypergeometric.upperCumulativeProbability(20), 1e-10);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(30), 1e-10);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(31), 1e-10);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(15.0, hypergeometric.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(7.959183673469388, hypergeometric.getNumericalVariance(), 1e-10);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(20, hypergeometric.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(30, hypergeometric.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometric.isSupportConnected());
    }
    
    @Test
    public void testSampling() {
        RandomGenerator rng1 = new Well19937c(0);
        HypergeometricDistribution h1 = new HypergeometricDistribution(rng1, 100, 50, 30);
        assertEquals(15, h1.sample(), 0.0);
        
        RandomGenerator rng2 = new Well19937c();
        HypergeometricDistribution h2 = new HypergeometricDistribution(rng2, 100, 50, 30);
        assertEquals(15, h2.sample(), 0.0);
    }
}