package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution hgd = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, hgd.getPopulationSize());
        assertEquals(5, hgd.getNumberOfSuccesses());
        assertEquals(2, hgd.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hgd = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.545454545, hgd.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hgd = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1, hgd.getNumericalMean(), 0.0001);
    }

    // Add more test cases for other methods as needed

}

