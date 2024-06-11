import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.probability(0), 0.00001);
        assertEquals(0.205212496559747, distribution.probability(1), 0.00001);
        assertEquals(0.2565156206996825, distribution.probability(2), 0.00001);

        assertEquals(0.0, distribution.probability(-1), 0.00001);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), 0.00001);
        assertEquals(0.28729749518364576, distribution.cumulativeProbability(1), 0.00001);
        assertEquals(0.5438131156773283, distribution.cumulativeProbability(2), 0.00001);

        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        try {
            assertEquals(0.10980292258604279, distribution.normalApproximateProbability(0), 0.00001);
            assertEquals(0.27406861906119786, distribution.normalApproximateProbability(1), 0.00001);
            assertEquals(0.4012936743170762, distribution.normalApproximateProbability(2), 0.00001);
        } catch (MathException e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        try {
            assertTrue(distribution.sample() <= Double.MAX_VALUE);
        } catch (MathException e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0);
    }

    @Test
    public void testConstructorIllegalArgumentException() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(0);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConstructorIllegalArgumentException2() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(-5);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConstructor() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
            assertEquals(5.0, distribution.getMean(), 0.0);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testConstructor2() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(5, 1e-10);
            assertEquals(5.0, distribution.getMean(), 0.0);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testConstructor3() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(5, 1e-10, 100);
            assertEquals(5.0, distribution.getMean(), 0.0);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

}