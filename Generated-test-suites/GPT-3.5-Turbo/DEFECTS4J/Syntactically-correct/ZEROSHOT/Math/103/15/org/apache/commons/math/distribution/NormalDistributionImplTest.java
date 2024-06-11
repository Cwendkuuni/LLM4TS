import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(dist.getMean(), 0.0, 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(2.5);
        assertEquals(dist.getMean(), 2.5, 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(dist.getStandardDeviation(), 1.0, 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(3.0);
        assertEquals(dist.getStandardDeviation(), 3.0, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double result = dist.cumulativeProbability(1.0);
        assertEquals(result, 0.841344, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double result = dist.inverseCumulativeProbability(0.5);
        assertEquals(result, 0.0, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double result = dist.getDomainLowerBound(0.3);
        assertEquals(result, -Double.MAX_VALUE, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double result = dist.getDomainUpperBound(0.3);
        assertEquals(result, Double.MAX_VALUE, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double result = dist.getInitialDomain(0.3);
        assertEquals(result, -0.385929, 0.0001);
    }
}