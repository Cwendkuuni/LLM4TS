import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        assertEquals(0, normalDist.getMean(), 0.0001);
        assertEquals(1, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCustomConstructor() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDist.getMean(), 0.0001);
        assertEquals(2, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804, normalDist.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, normalDist.cumulativeProbability(0), 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInvalidCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        normalDist.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1), 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        normalDist.inverseCumulativeProbability(2);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double sample = normalDist.sample();
        assertNotNull(sample);
    }
}