package org.apache.commons.math3.distribution;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hyperGeoDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.214, hyperGeoDist.cumulativeProbability(2), 0.001);
    }

    // Add test cases for other methods as well

}