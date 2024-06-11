package org.apache.commons.math3.distribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test the HypergeometricDistribution constructor with valid inputs
    @Test
    public void testConstructorValidInputs() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 5);
        
        assertEquals(2, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getSampleSize());
    }
    
    // Test the cumulativeProbability method for x < lower domain
    @Test
    public void testCumulativeProbabilityLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 5);
        
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
    }
}