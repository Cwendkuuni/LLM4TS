import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {
    
    // Create an instance of the class to test
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

    @Test
    public void testGetMean() {
        double expected = 0.0;
        double actual = normalDistribution.getMean();
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testSetMean() {
        double expected = 5.5;
        normalDistribution.setMean(expected);
        double actual = normalDistribution.getMean();
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        double expected = 1.0;
        double actual = normalDistribution.getStandardDeviation();
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        double expected = 2.5;
        normalDistribution.setStandardDeviation(expected);
        double actual = normalDistribution.getStandardDeviation();
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.0;
        double expected = 0.84134;
        double actual = normalDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double p = 0.25;
        double expected = -Double.MAX_VALUE;
        double actual = normalDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double p = 0.75;
        double expected = Double.MAX_VALUE;
        double actual = normalDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.00001);
    }
}