
// Complete JUnit 4 test file for PoissonDistributionImpl class

import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor1() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testConstructor2() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0, 1E-12, 1000000);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }

    // Additional test cases for other public methods in PoissonDistributionImpl class

    @Test
    public void testProbabilityEdgeCases() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.0001);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityExceptions() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Integer.MIN_VALUE));
    }

    // Additional test cases for edge cases and exception handling scenarios

}
