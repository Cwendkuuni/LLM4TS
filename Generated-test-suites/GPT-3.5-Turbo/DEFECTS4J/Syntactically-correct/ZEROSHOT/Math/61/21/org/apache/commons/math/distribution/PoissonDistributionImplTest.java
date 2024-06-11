import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.getMean() == 1.5;
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.probability(0) == 0.22313016014842982;
        assert distribution.probability(1) == 0.3346952402226449;
        assert distribution.probability(2) == 0.25102193016698364;
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.cumulativeProbability(0) == 0.22313016014842982;
        assert distribution.cumulativeProbability(1) == 0.5578254003710747;
        assert distribution.cumulativeProbability(2) == 0.8088473305380584;
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.normalApproximateProbability(0) == 0.18253686556821092;
        assert distribution.normalApproximateProbability(1) == 0.5321707298170228;
        assert distribution.normalApproximateProbability(2) == 0.7503110675006301;
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        int sample = distribution.sample();
        assert sample >= 0 && sample <= Integer.MAX_VALUE;
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.getDomainLowerBound(0.5) == 0;
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assert distribution.getDomainUpperBound(0.5) == Integer.MAX_VALUE;
    }
}