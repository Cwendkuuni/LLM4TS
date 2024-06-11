
import org.junit.Test;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assert distribution.getMean() == 3.5;
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assert distribution.probability(2) == Math.exp(-3.5) * (3.5 * 3.5) / MathUtils.TWO_PI;
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        try {
            assert distribution.cumulativeProbability(2) == Gamma.regularizedGammaQ(2.0 + 1, 3.5, 1E-12, 10000000);
        } catch (MathException e) {
            assert false;
        }
    }
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        try {
            assert distribution.normalApproximateProbability(2) == distribution.normal.cumulativeProbability(2 + 0.5);
        } catch (MathException e) {
            assert false;
        }
    }
    
    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        try {
            int sample = distribution.sample();
            assert sample >= 0 && sample <= Integer.MAX_VALUE;
        } catch (MathException e) {
            assert false;
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assert distribution.getDomainLowerBound(0.5) == 0;
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assert distribution.getDomainUpperBound(0.5) == Integer.MAX_VALUE;
    }
}
