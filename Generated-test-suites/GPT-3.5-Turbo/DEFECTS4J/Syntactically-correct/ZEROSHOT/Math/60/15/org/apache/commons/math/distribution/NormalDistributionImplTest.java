import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    // Test density method
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0.000001);
        assertEquals(0.02832703740497973, distribution.density(-1), 0.000001);
        assertEquals(0.010798819144210016, distribution.density(-2), 0.000001);
    }
    
    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.15865525393145707, distribution.cumulativeProbability(-1), 0.000001);
        assertEquals(0.02275013194817921, distribution.cumulativeProbability(-2), 0.000001);
    }
    
    // Test inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), 0.000001);
        assertEquals(-1, distribution.inverseCumulativeProbability(0.15865525393145707), 0.000001);
        assertEquals(-2, distribution.inverseCumulativeProbability(0.02275013194817921), 0.000001);
    }
    
    // Test sample method
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
    }
    
    // Test getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.000001);
    }
    
    // Test getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0.000001);
    }
}