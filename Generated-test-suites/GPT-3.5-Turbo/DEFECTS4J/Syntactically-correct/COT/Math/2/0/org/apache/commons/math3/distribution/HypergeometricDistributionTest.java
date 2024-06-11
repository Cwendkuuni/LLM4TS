
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
    
    // Test class instance
    private HypergeometricDistribution distribution;
    
    @Before
    public void setup() {
        // Initialize the test instance with valid parameters
        try {
            distribution = new HypergeometricDistribution(100, 50, 10);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Failed to initialize HypergeometricDistribution");
        }
    }

    @Test
    public void testLowerDomain() {
        // Lower domain when population size is 100, number of successes is 50, and sample size is 10
        assertEquals(40, distribution.getLowerDomain(100, 50, 10));
    }

    @Test
    public void testUpperDomain() {
        // Upper domain when number of successes is 50 and sample size is 10
        assertEquals(10, distribution.getUpperDomain(50, 10));
    }

    @Test
    public void testGetNumericalMean() {
        // Mean when population size is 100, number of successes is 50, and sample size is 10
        assertEquals(5.0, distribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Variance when population size is 100, number of successes is 50, and sample size is 10
        assertEquals(0.8333333333, distribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Lower bound of support when population size is 100, number of successes is 50, and sample size is 10
        assertEquals(40, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        // Upper bound of support when number of successes is 50 and sample size is 10
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Support of this distribution is connected
        assertTrue(distribution.isSupportConnected());
    }
}
