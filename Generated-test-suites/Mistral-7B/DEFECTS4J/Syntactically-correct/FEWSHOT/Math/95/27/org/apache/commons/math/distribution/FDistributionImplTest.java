package org.apache.commons.math.distribution;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        for (double x = 0.0; x <= 5.0; x += 0.5) {
            double prob = distribution.cumulativeProbability(x);
            System.out.printf("%.2f -> %.6f\n", x, prob);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        for (double p = 0.01; p <= 0.99; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            System.out.printf("%.2f -> %.6f\n", p, x);
        }
    }

    @Test
    public void testIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-1.0, 2.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(1.0, -1.0));
    }

    @Test
    public void testBetaFunction() throws MathException {
        for (int n = 0; n <= 10; n++) {
            for (int m = 0; m <= 10; m++) {
                double x = Beta.regularizedBeta((n * 2.5) / (m + n * 2.5), 0.5 * n, 0.5 * m);
                System.out.printf("n=%d, m=%d, x=%.6f\n", n, m, x);
            }
        }
    }
}