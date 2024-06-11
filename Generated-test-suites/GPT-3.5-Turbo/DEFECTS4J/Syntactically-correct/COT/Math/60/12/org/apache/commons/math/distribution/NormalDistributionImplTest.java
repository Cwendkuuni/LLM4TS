
import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normal;

    @Before
    public void setUp() throws Exception {
        normal = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normal.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normal.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normal.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normal.cumulativeProbability(0), 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normal.inverseCumulativeProbability(0.5), 1e-15);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normal.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
}
