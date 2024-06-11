package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor_WithValidParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertNotNull(distribution);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.2777777777777778, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.2222222222222222, distribution.probability(1), 0.0001);
    }

    // Other test cases for remaining methods...
}
