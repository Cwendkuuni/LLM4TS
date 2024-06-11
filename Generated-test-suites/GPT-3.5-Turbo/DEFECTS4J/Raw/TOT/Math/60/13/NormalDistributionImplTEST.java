import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    // Test cases for getMean() method
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(2, normal.getMean(), 0);
    }

    // Test cases for getStandardDeviation() method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(3, normal.getStandardDeviation(), 0);
    }

    // Test cases for density(double x) method
    @Test
    public void testDensityTypical() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(0.106482669, normal.density(4), 0.000000001);
    }
    
    @Test
    public void testDensityZeroMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 3);
        assertEquals(0.0752834537, normal.density(2), 0.000000001);
    }
    
    @Test
    public void testDensityZeroStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 0);
        assertEquals(0, normal.density(4), 0);
    }
    
    // Test cases for cumulativeProbability(double x) method
    @Test
    public void testCumulativeProbabilityTypical() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(0.894350226, normal.cumulativeProbability(5), 0.000000001);
    }
    
    @Test
    public void testCumulativeProbabilityNegativeInfinity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(0, normal.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
    }
    
    @Test
    public void testCumulativeProbabilityPositiveInfinity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(1, normal.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
    }
    
    // Test cases for inverseCumulativeProbability(double p) method
    @Test
    public void testInverseCumulativeProbabilityTypical() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(5.644853628, normal.inverseCumulativeProbability(0.8), 0.000000001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), 0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1), 0);
    }
    
    // Test cases for sample() method
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        double sample = normal.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    // Test cases for getDomainLowerBound(double p) method
    @Test
    public void testGetDomainLowerBoundTypical() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.3), 0);
    }
    
    @Test
    public void testGetDomainLowerBoundHalf() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(2, normal.getDomainLowerBound(0.5), 0);
    }
    
    // Test cases for getDomainUpperBound(double p) method
    @Test
    public void testGetDomainUpperBoundTypical() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.7), 0);
    }
    
    @Test
    public void testGetDomainUpperBoundHalf() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(2, normal.getDomainUpperBound(0.5), 0);
    }
    
    // Test cases for getInitialDomain(double p) method
    @Test
    public void testGetInitialDomainTypical() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(5, normal.getInitialDomain(0.8), 0);
    }
    
    @Test
    public void testGetInitialDomainHalf() {
        NormalDistributionImpl normal = new NormalDistributionImpl(2, 3);
        assertEquals(5, normal.getInitialDomain(0.5), 0);
    }
}