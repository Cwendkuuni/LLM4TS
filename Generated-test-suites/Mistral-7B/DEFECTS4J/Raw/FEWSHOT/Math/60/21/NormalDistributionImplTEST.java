package org.apache.commons.math.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDist.getMean(), 0.001);
        assertEquals(1, normalDist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplInvalidStandardDeviation() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        // Testing for some values close to mean and standard deviation
        double densityValue = normalDist.density(0.5);
        assertEquals(0.39894228, densityValue, 0.001);

        // Testing for values far from mean
        densityValue = normalDist.density(5);
        assertEquals(2.88687688E-12, densityValue, 0.001);

        densityValue = normalDist.density(-5);
        assertEquals(2.27198343E-12, densityValue, 0.001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);

        // Testing some values close to mean
        double cumProb = normalDist.cumulativeProbability(0.5);
        assertEquals(0.68268946, cumProb, 0.001);

        // Testing some values far from mean
        cumProb = normalDist.cumulativeProbability(5);
        assertEquals(0.00011342, cumProb, 0.001);

        cumProb = normalDist.cumulativeProbability(-5);
        assertEquals(0.99988654, cumProb, 0.001);

        // Testing with values very far from mean
        cumProb = normalDist.cumulativeProbability(-100);
        assertEquals(0, cumProb, Precision.EPSILON);

        cumProb = normalDist.cumulativeProbability(100);
        assertEquals(1, cumProb, Precision.EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImplCumulativeProbabilityWithMaxIterationsExceededException() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        normalDist.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);

        // Testing with probabilities close to 0 and 1
        double invCDF = normalDist.inverseCumulativeProbability(0.0);
        assertEquals(-Infinity.doubleValue(), invCDF, 0.001);

        invCDF = normalDist.inverseCumulativeProbability(1.0);
        assertEquals(Infinity.doubleValue(), invCDF, 0.001);

        // Testing with probabilities close to mean
        invCDF = normalDist.inverseCumulativeProbability(0.5);
        assertEquals(0, invCDF, 0.001);

        // Testing with probabilities far from mean
        invCDF = normalDist.inverseCumulativeProbability(0.01);
        assertEquals(-1.26166393, invCDF, 0.01);

        invCDF = normalDist.inverseCumulativeProbability(0.99);
        assertEquals(1.26166393, invCDF, 0.01);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        RandomData randomData = new RandomData();

        double sampleValue;
        for (int i = 0; i < 10; i++) {
            sampleValue = normalDist.sample();
            assertTrue(Double.isFinite(sampleValue));
            assertTrue(normalDist.density(sampleValue) > 0);
        }
    }
}