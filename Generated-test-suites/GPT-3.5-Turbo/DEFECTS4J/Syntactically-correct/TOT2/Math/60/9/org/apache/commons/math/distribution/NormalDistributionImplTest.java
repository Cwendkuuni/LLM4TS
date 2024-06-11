import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        assertEquals(5, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0), 1e-15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, distribution.inverseCumulativeProbability(0), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double sample = distribution.sample();
            System.out.println("Random sample: " + sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}