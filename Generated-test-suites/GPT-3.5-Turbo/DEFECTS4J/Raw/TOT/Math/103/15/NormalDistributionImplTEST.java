import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDistribution.getMean(), 0.001);
    }

    @Test
    public void testSetMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        normalDistribution.setMean(10);
        assertEquals(10, normalDistribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        normalDistribution.setStandardDeviation(3);
        assertEquals(3, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        try {
            assertEquals(0.308537, normalDistribution.cumulativeProbability(3), 0.001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        try {
            assertEquals(-0.67448, normalDistribution.inverseCumulativeProbability(0.25), 0.001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        try {
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0.001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0.001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(3, normalDistribution.getInitialDomain(0.5), 0.001);
    }
}