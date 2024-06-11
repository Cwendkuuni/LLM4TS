import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.24197, distribution.density(0), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, distribution.inverseCumulativeProbability(0), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double sample = distribution.sample();
            System.out.println("Sampled value: " + sample);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}