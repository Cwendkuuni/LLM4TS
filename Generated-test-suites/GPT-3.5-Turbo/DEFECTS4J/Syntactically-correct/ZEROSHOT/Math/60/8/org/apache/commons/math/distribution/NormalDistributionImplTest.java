import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(0.1994711402, distribution.density(1.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(0.8413447461, distribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(1.0, distribution.inverseCumulativeProbability(0.8413447461), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
}