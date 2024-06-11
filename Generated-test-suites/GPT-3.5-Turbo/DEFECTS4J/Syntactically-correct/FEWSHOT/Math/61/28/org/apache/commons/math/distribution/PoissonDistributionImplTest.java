import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {
    private static final double EPSILON = 1E-10;

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), EPSILON);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);

        assertEquals(0.0820849986238988, distribution.probability(0), EPSILON);
        assertEquals(0.205212496559747, distribution.probability(1), EPSILON);
        assertEquals(0.256515620699684, distribution.probability(2), EPSILON);
        assertEquals(0.213763017249737, distribution.probability(3), EPSILON);
        assertEquals(0.133601260781086, distribution.probability(4), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);

        assertEquals(0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.287297495183645, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.543813115883329, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(0.757576133133067, distribution.cumulativeProbability(3), EPSILON);
        assertEquals(0.891177393914154, distribution.cumulativeProbability(4), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);

        assertEquals(0, distribution.normalApproximateProbability(-1), EPSILON);
        assertEquals(0.0545656489842276, distribution.normalApproximateProbability(0), EPSILON);
        assertEquals(0.270908710448681, distribution.normalApproximateProbability(1), EPSILON);
        assertEquals(0.678654708812590, distribution.normalApproximateProbability(2), EPSILON);
        assertEquals(0.913827986564366, distribution.normalApproximateProbability(3), EPSILON);
        assertEquals(0.983790458590774, distribution.normalApproximateProbability(4), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}