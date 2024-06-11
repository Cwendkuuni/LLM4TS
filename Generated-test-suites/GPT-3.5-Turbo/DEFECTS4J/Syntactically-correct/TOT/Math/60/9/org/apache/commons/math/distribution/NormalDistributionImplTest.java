import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double EPSILON = 1e-10;

    private NormalDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, distribution.density(0.0), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0.0), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), EPSILON);
    }
}