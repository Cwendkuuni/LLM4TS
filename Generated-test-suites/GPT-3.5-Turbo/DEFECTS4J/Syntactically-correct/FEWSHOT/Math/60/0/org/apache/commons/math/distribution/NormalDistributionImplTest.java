import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistribution distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, distribution.density(0), 0.0000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(-Infinity, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Infinity, distribution.inverseCumulativeProbability(1), 0);

        double x = distribution.inverseCumulativeProbability(0.5);
        assertTrue(x >= -0.67448975 && x <= 0.67448975);
    }

    @Test
    public void testSample() throws MathException {
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
}