package org.apache.commons.math.distribution;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplCreation() {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImplNegativeNumeratorDoF() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-3.0, 5.0));
    }

    @Test
    public void testFDistributionImplNegativeDenominatorDoF() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(3.0, -5.0));
    }

    @Test
    public void testFDistributionImplCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        double x = 1.5;
        double p = distribution.cumulativeProbability(x);
        assertEquals(0.8714285714285713, p, 0.00001);

        x = 2.5;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.9757811023558979, p, 0.00001);

        x = 4.5;
        p = distribution.cumulativeProbability(x);
        assertEquals(1.0, p, 0.00001);

        x = 0.5;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.04834189764977638, p, 0.00001);
    }

    @Test
    public void testFDistributionImplInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        double p = 0.9;
        double x = distribution.inverseCumulativeProbability(p);
        assertEquals(2.231368125685522, x, 0.00001);

        p = 0.1;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.635635767886428, x, 0.00001);

        p = 0.5;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(1.229708641623113, x, 0.00001);
    }
}