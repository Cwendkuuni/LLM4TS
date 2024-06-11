import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(2);
        assert distribution.getMean() == 2;
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(2);
        assert distribution.getMean() == 2;
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        assert distribution.getStandardDeviation() == 3;
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        assert distribution.getStandardDeviation() == 3;
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(1);
        assert Math.abs(result - 0.841344746068543) < 0.00001;
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.cumulativeProbability(21);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.inverseCumulativeProbability(0.5);
        assert result == 0;
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.inverseCumulativeProbability(0);
        assert Double.isInfinite(result) && result < 0;
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.inverseCumulativeProbability(1);
        assert Double.isInfinite(result) && result > 0;
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.getDomainLowerBound(0.2);
        assert result == -Double.MAX_VALUE;
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.getDomainUpperBound(0.8);
        assert result == Double.MAX_VALUE;
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.getInitialDomain(0.5);
        assert result == 0;
    }

}