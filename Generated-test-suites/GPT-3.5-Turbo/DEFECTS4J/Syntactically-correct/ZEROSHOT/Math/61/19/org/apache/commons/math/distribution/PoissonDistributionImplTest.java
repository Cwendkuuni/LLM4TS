import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1e-10;
    private static final int MAX_ITERATIONS = 1000000;

    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(5.0, EPSILON, MAX_ITERATIONS);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.0, poisson.getMean(), EPSILON);
    }

    @Test
    public void testProbability() {
        assertEquals(0.006737946999085467, poisson.probability(2), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.9862041492072364, poisson.cumulativeProbability(9), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.9937903346742239, poisson.normalApproximateProbability(10), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int sample = poisson.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poisson.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
    }

    @Test
    public void testIllegalArguments() {
        try {
            poisson = new PoissonDistributionImpl(0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            poisson = new PoissonDistributionImpl(-1.0, EPSILON, MAX_ITERATIONS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

}