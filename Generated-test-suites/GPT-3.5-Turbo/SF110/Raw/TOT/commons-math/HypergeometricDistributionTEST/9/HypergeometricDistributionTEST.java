import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, 2);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
        assertEquals(0.2, distribution.cumulativeProbability(1), 0.0);
        assertEquals(0.6, distribution.cumulativeProbability(2), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0);
    }

    @Test
    public void testGetLowerDomain() {
        assertEquals(0, distribution.getLowerDomain(10, 5, 2));
        assertEquals(2, distribution.getLowerDomain(10, 3, 2));
        assertEquals(0, distribution.getLowerDomain(10, 7, 8));
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(0), 0.0);
        assertEquals(0.4, distribution.probability(1), 0.0);
        assertEquals(0.4, distribution.probability(2), 0.0);
        assertEquals(0.0, distribution.probability(3), 0.0);
        assertEquals(0.0, distribution.probability(4), 0.0);
        assertEquals(0.0, distribution.probability(5), 0.0);
        assertEquals(0.0, distribution.probability(6), 0.0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0);
        assertEquals(0.6, distribution.upperCumulativeProbability(2), 0.0);
        assertEquals(0.2, distribution.upperCumulativeProbability(3), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.0, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.2222222222222222, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }    
}