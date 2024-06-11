
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double mean = normalDist.getMean();
        assertEpsilonEquals(mean, 0.0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.setMean(2.0);
        double mean = normalDist.getMean();
        assertEpsilonEquals(mean, 2.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanInvalid() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.setMean(-1.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double sd = normalDist.getStandardDeviation();
        assertEpsilonEquals(sd, 1.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.setStandardDeviation(2.0);
        double sd = normalDist.getStandardDeviation();
        assertEpsilonEquals(sd, 2.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.setStandardDeviation(0.0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double prob = normalDist.cumulativeProbability(0.0);
        assertEpsilonEquals(prob, 0.5);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double critPoint = normalDist.inverseCumulativeProbability(0.5);
        assertEpsilonEquals(critPoint, 0.0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityLowerBound() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double critPoint = normalDist.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, critPoint, 0.0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityUpperBound() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double critPoint = normalDist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, critPoint, 0.0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double lowerBound = normalDist.getDomainLowerBound(0.25);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double upperBound = normalDist.getDomainUpperBound(0.75);
        assertEquals(Double.MAX_VALUE, upperBound, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double initialDomain = normalDist.getInitialDomain(0.5);
        assertEpsilonEquals(initialDomain, 1.0);
    }
    
    private void assertEpsilonEquals(double actual, double expected) {
        double epsilon = 1e-6;
        assertEquals(actual, expected, epsilon);
    }
}
