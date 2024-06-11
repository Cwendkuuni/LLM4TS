import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);
        assertEquals(5, poisson.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3);
        assertEquals(0.049787068367863944, poisson.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
        assertEquals(0.23810330555354436, poisson.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4);
        assertEquals(0.7626606927903315, poisson.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3);
        int sample = poisson.sample();
        assertTrue(sample >= 0);
    }

}