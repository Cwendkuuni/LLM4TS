
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(1.96);
        assertEquals(0.9750021048517795, cumulativeProbability, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.975);
        assertEquals(1.959964, inverseCumulativeProbability, 0.0001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertNotNull(sample);
    }
}
