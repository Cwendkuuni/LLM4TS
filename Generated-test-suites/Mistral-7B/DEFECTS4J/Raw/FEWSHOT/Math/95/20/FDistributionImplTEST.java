package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FTest;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        for (double x = 0.0; x <= 4.5; x += 0.1) {
            double cdf = fd.cumulativeProbability(x);
            assertEquals(cdf, FTest.cumulativeDistributionF(x, 5.0, 10.0), 0.0001);
        }
        assertThrows(MathException.class, () -> fd.cumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = fd.inverseCumulativeProbability(p);
            assertEquals(x, FTest.inverseCumulativeDistributionF(p, 5.0, 10.0), 0.0001);
        }
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setNumeratorDegreesOfFreedom(7.0);
        assertEquals(7.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setDenominatorDegreesOfFreedom(15.0);
        assertEquals(15.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }

}