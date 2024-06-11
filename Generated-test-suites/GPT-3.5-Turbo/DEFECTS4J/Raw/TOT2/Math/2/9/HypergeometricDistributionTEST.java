import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution hypergeometricDistribution;

    @Before
    public void setUp() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.407, hypergeometricDistribution.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testGetDomain() {
        int[] domain = hypergeometricDistribution.getDomain(10, 5, 3);
        assertEquals(2, domain[0]);
        assertEquals(3, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        assertEquals(2, hypergeometricDistribution.getLowerDomain(10, 5,3));
    }

    @Test
    public void testGetUpperDomain() {
        assertEquals(3, hypergeometricDistribution.getUpperDomain(5,3));
    }

    @Test
    public void testProbability() {
        assertEquals(0.357, hypergeometricDistribution.probability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.593, hypergeometricDistribution.upperCumulativeProbability(2), 0.001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        assertEquals(0.357, hypergeometricDistribution.innerCumulativeProbability(2, 3, 1), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.75, hypergeometricDistribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(0, 5, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }
}