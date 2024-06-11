import org.junit.Test;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assert distribution.getMean() == 2;
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3);
        assert distribution.getMean() == 3;
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assert distribution.getStandardDeviation() == 1;
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2);
        assert distribution.getStandardDeviation() == 2;
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double result = distribution.cumulativeProbability(3);
        assert result > 0;
        assert result < 1;
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double result = distribution.inverseCumulativeProbability(0.5);
        assert result == 2;
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double result = distribution.getDomainLowerBound(0.3);
        assert result == -Double.MAX_VALUE;
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double result = distribution.getDomainUpperBound(0.3);
        assert result == Double.MAX_VALUE;
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double result = distribution.getInitialDomain(0.3);
        assert result == 1;
    }
}