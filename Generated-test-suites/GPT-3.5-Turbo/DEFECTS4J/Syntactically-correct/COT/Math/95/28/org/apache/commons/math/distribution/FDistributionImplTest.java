import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        double x = 1.0;
        double cumulativeProb = distribution.cumulativeProbability(x);
        assert cumulativeProb >= 0.0;
        assert cumulativeProb <= 1.0;
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        double p = 0.5;
        double inverseCumulativeProb = distribution.inverseCumulativeProbability(p);
        assert inverseCumulativeProb >= 0.0;
        assert inverseCumulativeProb <= Double.POSITIVE_INFINITY;
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        double p = 0.5;
        double domainLowerBound = distribution.getDomainLowerBound(p);
        assert domainLowerBound == 0.0;
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        double p = 0.5;
        double domainUpperBound = distribution.getDomainUpperBound(p);
        assert domainUpperBound == Double.MAX_VALUE;
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        double p = 0.5;
        double initialDomain = distribution.getInitialDomain(p);
        assert initialDomain > 0.0;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        distribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        distribution.setDenominatorDegreesOfFreedom(-1);
    }
}