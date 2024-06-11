import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(0.00876415024678427, normalDistribution.density(8), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(0.9331927987311419, normalDistribution.cumulativeProbability(8), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_MaxIterationsExceededException() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1000, 100);
        
        // When the value is extremely far from mean, the cumulative probability should be close to 0 or 1
        assertEquals(1.0, normalDistribution.cumulativeProbability(2000), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(8.482135131081928, normalDistribution.inverseCumulativeProbability(0.9), 0.0001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInverseCumulativeProbability_InvalidProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        normalDistribution.inverseCumulativeProbability(-0.5);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        double sample = normalDistribution.sample();
        assertTrue(sample >= 0 && sample <= 10);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(1e-9, normalDistribution.getSolverAbsoluteAccuracy(), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.1), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.9), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(7, normalDistribution.getInitialDomain(0.6), 0.0001);
    }
}