import org.junit.Test;
import org.apache.commons.math.MathException;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.22404180765538776, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4);
        assertEquals(0.23810330555354436, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.4825307445855016, distribution.normalApproximateProbability(3), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7);
        assertEquals(0, distribution.getDomainLowerBound(0.2));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(8);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.8));
    }
}