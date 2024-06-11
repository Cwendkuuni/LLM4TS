package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDegreesOfFreedom() {
        try {
            new FDistribution(-1, 2);
            fail("Exception expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new FDistribution(1, -2);
            fail("Exception expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(5, 10);
        double[] x = new double[]{0.1, 0.5, 1.0, 2.0, 3.0};
        double[] expected = new double[]{0.0478249342895588, 0.163934455285728, 0.232245148370843, 0.163934455285728, 0.0478249342895588};

        for (int i = 0; i < x.length; i++) {
            double result = dist.density(x[i]);
            assertEquals(expected[i], result, 0.0001);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(5, 10);
        double[] x = new double[]{0.1, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] expected = new double[]{0.0686508953423485, 0.212812208330359, 0.316227766016837, 0.418318602740997, 0.500000000000000, 0.581692346924816, 0.664186575965457, 0.741329338597805};

        for (int i = 0; i < x.length; i++) {
            double result = dist.cumulativeProbability(x[i]);
            assertEquals(expected[i], result, 0.0001);
        }
    }

    @Test
    public void testFDistributionMean() {
        FDistribution dist = new FDistribution(5, 10);
        double expected = 6.2;
        double result = dist.getNumericalMean();
        assertEquals(expected, result, 0.01);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution dist = new FDistribution(5, 10);
        double expected = 10.84;
        double result = dist.getNumericalVariance();
        assertEquals(expected, result, 0.01);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution dist = new FDistribution(5, 10);
        dist.inverseCumulativeProbability(0.5);
        fail("Exception expected");
    }

    @Test
    public void testFDistributionRandomValues() {
        FDistribution dist = new FDistribution(5, 10);
        Random rng = new Random();

        for (int i = 0; i < 100; i++) {
            double x = dist.inverseCumulativeProbability(rng.nextDouble());
            assertTrue(dist.supportContains(x));
        }
    }
}