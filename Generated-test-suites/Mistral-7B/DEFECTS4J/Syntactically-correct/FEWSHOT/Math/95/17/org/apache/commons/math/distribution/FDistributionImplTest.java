package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_ValidConstruction() {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImpl_NullConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(null, 4.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(3.0, null));
    }

    @Test
    public void testFDistributionImpl_InvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-1.0, 4.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(3.0, -1.0));
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);

        assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0);
        assertEquals(0.25, dist.cumulativeProbability(1.25), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(1.5), 0.001);
        assertEquals(0.75, dist.cumulativeProbability(2.25), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(3.0), 0.001);

        assertThrows(MathException.class, () -> dist.cumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> dist.cumulativeProbability(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);

        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(1.25, dist.inverseCumulativeProbability(0.25), 0.001);
        assertEquals(1.5, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(2.25, dist.inverseCumulativeProbability(0.75), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(0.9));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.0));
    }

    @Test
    public void testInverseCumulativeProbabilityWithType() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);

        FDistType type = FDistType.F2;
        assertEquals(1.868651239609375, dist.inverseCumulativeProbability(0.95), 0.001);

        type = FDistType.F;
        assertEquals(1.682824601828032, dist.inverseCumulativeProbability(0.95), 0.001);
    }
}