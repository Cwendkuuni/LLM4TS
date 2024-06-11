import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1e-12;
    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(2.5);
    }

    @Test
    public void testGetMean() {
        assertEquals(2.5, distribution.getMean(), EPSILON);
    }

    @Test
    public void testProbability() {
        // typical use cases
        assertEquals(0.0820849986238988, distribution.probability(0), EPSILON);
        assertEquals(0.205212496559747, distribution.probability(1), EPSILON);
        assertEquals(0.2137665008130365, distribution.probability(2), EPSILON);

        // edge cases
        assertEquals(0.03280856373538141, distribution.probability(Integer.MAX_VALUE), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // typical use cases
        assertEquals(0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.2872974951836458, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.5010639969966824, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(0.6922006275553467, distribution.cumulativeProbability(3), EPSILON);
        
        // edge cases
        assertEquals(1, distribution.cumulativeProbability(Integer.MAX_VALUE), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        // typical use cases
        assertEquals(0.857123460498547, distribution.normalApproximateProbability(0), EPSILON);
        assertEquals(0.9758721563303934, distribution.normalApproximateProbability(1), EPSILON);
        assertEquals(0.997146742084094, distribution.normalApproximateProbability(2), EPSILON);
        assertEquals(0.9997673709209645, distribution.normalApproximateProbability(3), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        // test range
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0), EPSILON);
        assertEquals(0, distribution.getDomainLowerBound(1), EPSILON);
        assertEquals(0, distribution.getDomainLowerBound(0.99), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0), EPSILON);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1), EPSILON);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.99), EPSILON);
    }
}