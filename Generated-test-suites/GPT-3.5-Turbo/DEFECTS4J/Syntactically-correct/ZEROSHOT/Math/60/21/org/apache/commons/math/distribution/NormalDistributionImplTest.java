import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double mean = normal.getMean();
        assertEquals(0.0, mean, 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double sd = normal.getStandardDeviation();
        assertEquals(1.0, sd, 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double density = normal.density(0.0);
        assertEquals(0.3989, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double prob = normal.cumulativeProbability(1.0);
        assertEquals(0.8413, prob, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double invProb = normal.inverseCumulativeProbability(0.5);
        assertEquals(0.0, invProb, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double sample = normal.sample();
        assertNotNull(sample);
    }
}