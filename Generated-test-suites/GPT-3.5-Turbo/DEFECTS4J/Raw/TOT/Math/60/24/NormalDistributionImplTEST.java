import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-9;

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
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
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0.5), DELTA);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveStandardDeviation() {
        normalDistribution = new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertEquals(0, sample, 10);
    }

}