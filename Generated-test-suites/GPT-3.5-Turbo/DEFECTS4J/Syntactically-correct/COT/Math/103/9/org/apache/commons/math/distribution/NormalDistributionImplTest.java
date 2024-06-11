
import org.junit.Test;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assert(distribution.getMean() == 0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setMean(2);
        assert(distribution.getMean() == 2);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assert(distribution.getStandardDeviation() == 1);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setStandardDeviation(2);
        assert(distribution.getStandardDeviation() == 2);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double probability = distribution.cumulativeProbability(1);
            assert(probability == 0.8413447460685429);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double inverseProbability = distribution.inverseCumulativeProbability(0.5);
            assert(inverseProbability == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double lowerBound = distribution.getDomainLowerBound(0.7);
        assert(lowerBound == 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double upperBound = distribution.getDomainUpperBound(0.3);
        assert(upperBound == 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double initialDomain = distribution.getInitialDomain(0.5);
        assert(initialDomain == 0);
    }
}
