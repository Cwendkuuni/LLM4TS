import org.junit.Test;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        assert (Math.abs(poisson.probability(0) - 0.1353352832) < 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        try {
            assert (Math.abs(poisson.cumulativeProbability(3) - 0.8571234605) < 1e-9);
        } catch (MathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        try {
            assert (Math.abs(poisson.normalApproximateProbability(3) - 0.7750203051) < 1e-9);
        } catch (MathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        try {
            int sample = poisson.sample();
            assert (sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        assert (poisson.getMean() == 2.0);
    }
}