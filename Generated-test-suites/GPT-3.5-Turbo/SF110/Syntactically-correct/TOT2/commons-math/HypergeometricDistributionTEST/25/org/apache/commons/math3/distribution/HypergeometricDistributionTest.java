package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 4);
        assertEquals(3, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 4);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 4);
        assertEquals(0.21428571428571427, distribution.cumulativeProbability(2), 0.0001);
    }

    // Add more test cases for other public methods including edge cases and error scenarios

}
