import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, normal.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, normal.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.24197072451914337, normal.density(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, normal.cumulativeProbability(0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, normal.inverseCumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertNotNull(normal.sample());
    }
}