import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
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
        assertEquals(0.3989422804014327, distribution.density(0), 0.0001);
        assertEquals(0.24197072451914337, distribution.density(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
        assertEquals(0.841344746068543, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(1, distribution.inverseCumulativeProbability(0.841344746068543), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

}