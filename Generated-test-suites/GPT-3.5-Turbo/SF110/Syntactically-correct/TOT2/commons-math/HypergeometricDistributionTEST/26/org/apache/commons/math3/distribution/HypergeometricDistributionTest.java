package org.apache.commons.math3.distribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, hypergeometricDistribution.getNumberOfSuccesses());
    }
}