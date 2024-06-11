package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.distribution.FDistribution;
import org.apache.commons.math3.stat.distribution.FDistribution.Type1F;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_Constructor() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = fd.cumulativeProbability(x);
            assertEquals("Expected CDF to be monotonically increasing", x < 5.0, cdf < fd.cumulativeProbability(x + 0.1), 0.0);
            System.out.println(String.format("x: %.2f, cdf: %.6f", x, cdf));
        }
    }

    @Test
    public void testCumulativeProbability_NegativeInput() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertThrows(MathException.class, () -> fd.cumulativeProbability(-1.0));
    }

    @Test
    public void testCumulativeProbability_ZeroInput() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        double cdf = fd.cumulativeProbability(0.0);
        assertEquals(0.0, cdf, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability_ValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = fd.inverseCumulativeProbability(p);
            assertEquals("Expected x to be monotonically increasing", p < 1.0, x < fd.inverseCumulativeProbability(p + 0.1), 0.0);
            System.out.println(String.format("p: %.2f, x: %.6f", p, x));
        }
    }

    @Test
    public void testInverseCumulativeProbability_OutOfRangeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertThrows(IllegalArgumentException.class, () -> fd.inverseCumulativeProbability(-1.0));
        assertThrows(IllegalArgumentException.class, () -> fd.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
        assertEquals(-1.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_NegativeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
        assertEquals(-1.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_NegativeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testToString() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        String stringRepresentation = fd.toString();
        String expectedStringRepresentation = "FDistributionImpl[numeratorDegreesOfFreedom=2.0, denominatorDegreesOfFreedom=4.0]";
        assertEquals(expectedStringRepresentation, stringRepresentation);
    }

    @Test
    public void testGetType() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(Type1F.F, fd.getType());
    }
}