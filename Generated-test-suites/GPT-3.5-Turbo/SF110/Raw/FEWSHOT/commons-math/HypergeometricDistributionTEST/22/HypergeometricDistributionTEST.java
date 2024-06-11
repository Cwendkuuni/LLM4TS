package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_OF_SUCCESSES = 30;
    private static final int SAMPLE_SIZE = 10;
    private static HypergeometricDistribution distribution;

    @BeforeClass
    public static void setUp() throws Exception {
        distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
    }

    @Test
    public void testConstructor() {
        assertEquals(POPULATION_SIZE, distribution.getPopulationSize());
        assertEquals(NUMBER_OF_SUCCESSES, distribution.getNumberOfSuccesses());
        assertEquals(SAMPLE_SIZE, distribution.getSampleSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPopulationSize() throws Exception {
        new HypergeometricDistribution(-1, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() throws Exception {
        new HypergeometricDistribution(POPULATION_SIZE, -1, SAMPLE_SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidSampleSize() throws Exception {
        new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, -1);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.6325030741760722, distribution.cumulativeProbability(5), 1e-15);
        assertEquals(1.0, distribution.cumulativeProbability(10), 1e-15);
        assertEquals(1.0, distribution.cumulativeProbability(30), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3.0, distribution.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.8263473053892226, distribution.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertEquals(true, distribution.isSupportConnected());
    }
}