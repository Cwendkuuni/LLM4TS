package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.descript.moment.Mean;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.stat.statistic.DescriptiveStatistics;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplCreation() {
        FDistribution fd = new FDistributionImpl(2.5, 4.5);
        assertEquals(2.5, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.5, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(2.5, 4.5);
        double p = 0.05;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(0.05, fd.cumulativeProbability(x), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(2.5, 4.5);
        double p = 0.05;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(x, fd.cumulativeProbability(x), 0.001);
    }

    @Test
    public void testNegativeNumeratorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-1.5, 4.5));
    }

    @Test
    public void testNegativeDenominatorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(2.5, -1.5));
    }

    @Test
    public void testZeroNumeratorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(0.0, 4.5));
    }

    @Test
    public void testZeroDenominatorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(2.5, 0.0));
    }

    @Test
    public void testCumulativeProbabilityAtZero() throws MathException {
        FDistribution fd = new FDistributionImpl(2.5, 4.5);
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testMean() {
        FDistribution fd = new FDistributionImpl(1.5, 2.5);
        RealDistribution normal = new org.apache.commons.math3.distribution.NormalDistribution(
            fd.getMean(), new org.apache.commons.math3.util.Precision(0.001));
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < 100000; i++) {
            stats.addValue(fd.sample());
        }
        Mean mean = stats.getMean();
        assertEquals(fd.getMean(), mean.getMean(), 0.001);
        assertEquals(normal.getMean(), mean.getMean(), 0.001);
    }

    @Test
    public void testStandardDeviation() {
        FDistribution fd = new FDistributionImpl(1.5, 2.5);
        RealDistribution normal = new org.apache.commons.math3.distribution.NormalDistribution(
            fd.getMean(), new org.apache.commons.math3.util.Precision(0.001));
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < 100000; i++) {
            stats.addValue(fd.sample());
        }
        StandardDeviation stddev = stats.getStandardDeviation();
        assertEquals(fd.getStandardDeviation(), stddev.getStandardDeviation(), 0.001);
        assertEquals(normal.getStandardDeviation(), stddev.getStandardDeviation(), 0.001);
    }
}