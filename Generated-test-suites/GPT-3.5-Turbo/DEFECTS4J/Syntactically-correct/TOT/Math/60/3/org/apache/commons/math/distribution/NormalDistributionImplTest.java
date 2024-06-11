import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-9;
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testDensity_typicalCase() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), DELTA);
    }

    @Test
    public void testDensity_edgeCase() {
        assertEquals(0, normalDistribution.density(Double.NEGATIVE_INFINITY), DELTA);
    }

    @Test
    public void testCumulativeProbability_typicalCase() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), DELTA);
    }

    @Test
    public void testCumulativeProbability_edgeCase() throws MathException {
        assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability_typicalCase() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability_edgeCase() throws MathException {
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), DELTA);
    }

    @Test
    public void testSample_typicalCase() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testSample_errorCase() {
        assertThrows(MathException.class, () -> normalDistribution.sample());
    }
}