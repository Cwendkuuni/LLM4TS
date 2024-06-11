import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    // Test probability()
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.probability(0), 0.0001);
        assertEquals(0.205212496559747, distribution.probability(1), 0.0001);
        assertEquals(0.256515620699684, distribution.probability(2), 0.0001);
        assertEquals(0.2137628255830705, distribution.probability(3), 0.0001);
        assertEquals(0.1336017666144191, distribution.probability(4), 0.0001);
    }
    
    // Test cumulativeProbability()
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.287297495183646, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.5438131158833303, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.7575759414664008, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.8911777080808199, distribution.cumulativeProbability(4), 0.0001);
    }
    
    // Test normalApproximateProbability()
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.885401729216, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.737561683773, distribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.526872974840, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.322743036307, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.172156803747, distribution.normalApproximateProbability(4), 0.0001);
    }
    
    // Test sample()
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
    
    // Test getMean()
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
    
    // Test getDomainLowerBound()
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0, distribution.getDomainLowerBound(0.1));
        assertEquals(0, distribution.getDomainLowerBound(0.5));
        assertEquals(0, distribution.getDomainLowerBound(1.0));
    }
    
    // Test getDomainUpperBound()
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.1));
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0));
    }
    
    // Test constructor IllegalArgumentException when p <= 0
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentException1() {
        new PoissonDistributionImpl(-2.5);
    }
    
    // Test constructor IllegalArgumentException when p <= 0
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentException2() {
        new PoissonDistributionImpl(0);
    }
    
    // Test cumulativeProbability() throws MathException
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMathException() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }

}