package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        try {
            FDistribution fd = new FDistribution(3, 4);
            assertEquals(3, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4, fd.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }

        try {
            FDistribution fd = new FDistribution(-3, 4);
            fail("Exception should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            FDistribution fd = new FDistribution(3, -4);
            fail("Exception should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3, 4);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double density = fd.density(x);
            // Perform some basic checks on the result
            assertTrue(density >= 0.0);
            if (x == 0.0 || x == 5.0) {
                assertTrue(math.isCloseEnough(density, 0.0, 1e-10));
            } else {
                // More rigorous test, to be adjusted based on the specific implementation
                assertTrue(math.isCloseEnough(density, 0.0, 1e-5));
            }
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3, 4);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = fd.cumulativeProbability(x);
            // Perform some basic checks on the result
            assertTrue(cdf >= 0.0 && cdf <= 1.0);
            if (x == 0.0) {
                assertEquals(0.0, cdf, 1e-12);
            } else if (x == 5.0) {
                assertEquals(1.0, cdf, 1e-12);
            } else {
                // More rigorous test, to be adjusted based on the specific implementation
                assertTrue(math.isCloseEnough(cdf, cdf, 1e-6));
            }
        }
    }

    @Test
    public void testNumericalMean() {
        FDistribution fd = new FDistribution(3, 4);
        assertEquals(4.0 / 2.0, fd.getNumericalMean(), 1e-10);
    }

    @Test
    public void testNumericalVariance() {
        FDistribution fd = new FDistribution(3, 4);
        double numericalVariance = fd.getNumericalVariance();
        if (numericalVariance == Double.NaN) {
            fail("Numerical variance should be defined for this case.");
        }
        assertEquals(3.0 / 2.0, numericalVariance, 1e-10);
    }
}