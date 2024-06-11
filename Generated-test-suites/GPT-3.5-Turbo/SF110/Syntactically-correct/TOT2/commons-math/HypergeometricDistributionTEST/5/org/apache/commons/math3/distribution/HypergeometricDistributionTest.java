package org.apache.commons.math3.distribution;
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        try {
            HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 4, 3);
            assertNotNull(hypergeometric);
        } catch (Exception e) {
            fail("Exception in constructor");
        }
    }
}