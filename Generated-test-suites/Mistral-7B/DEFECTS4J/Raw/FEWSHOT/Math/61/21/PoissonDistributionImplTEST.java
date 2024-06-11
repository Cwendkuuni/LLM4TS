package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.13293383523351878, dist.probability(0), 1e-10);
        assertEquals(0.18266536863247526, dist.probability(1), 1e-10);
        assertEquals(0.25958341046138834, dist.probability(2), 1e-10);
        assertEquals(0.23409255123411533, dist.probability(3), 1e-10);
        assertEquals(0.17317793422173895, dist.probability(4), 1e-10);
        assertEquals(0.1281475467133186, dist.probability(5), 1e-10);
        assertEquals(0.08824944814483232, dist.probability(6), 1e-10);
        assertEquals(0.06007223192288308, dist.probability(7), 1e-10);
        assertEquals(0.04113733431128547, dist.probability(8), 1e-10);
        assertEquals(0.02942889509510286, dist.probability(9), 1e-10);
        assertEquals(0.02043549213263328, dist.probability(10), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(Integer.MIN_VALUE), 1e-10);
        assertEquals(0.13293383523351878, dist.cumulativeProbability(0), 1e-10);
        assertEquals(0.26586767046703756, dist.cumulativeProbability(1), 1e-10);
        assertEquals(0.40040112669955612, dist.cumulativeProbability(2), 1e-10);
        assertEquals(0.5385317131365868, dist.cumulativeProbability(3), 1e-10);
        assertEquals(0.6817607165726175, dist.cumulativeProbability(4), 1e-10);
        assertEquals(0.8292109350186484, dist.cumulativeProbability(5), 1e-10);
        assertEquals(0.9578272525566795, dist.cumulativeProbability(6), 1e-10);
        assertEquals(0.9963365755898659, dist.cumulativeProbability(7), 1e-10);
        assertEquals(0.9999192357082266, dist.cumulativeProbability(8), 1e-10);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeX() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        dist.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.13320834755576251, dist.normalApproximateProbability(0), 1e-10);
        assertEquals(0.1826346854383342, dist.normalApproximateProbability(1), 1e-10);
        assertEquals(0.2595833281409422, dist.normalApproximateProbability(2), 1e-10);
        assertEquals(0.2340924671167552, dist.normalApproximateProbability(3), 1e-10);
        assertEquals(0.1731778952663284, dist.normalApproximateProbability(4), 1e-10);
        assertEquals(0.1281475356142727, dist.normalApproximateProbability(5), 1e-10);
        assertEquals(0.0882494431052195, dist.normalApproximateProbability(6), 1e-10);
        assertEquals(0.06007223054713507, dist.normalApproximateProbability(7), 1e-10);
        assertEquals(0.04113733342992071, dist.normalApproximateProbability(8), 1e-10);
        assertEquals(0.02942889363260658, dist.normalApproximateProbability(9), 1e-10);
        assertEquals(0.02043549149595646, dist.normalApproximateProbability(10), 1e-10);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 10000; i++) {
            assertEquals(dist.sample(), dist.sample());
            int sample = dist.sample();
            assertEquals(dist.probability(sample), dist.probability(sample), 1e-10);
            assertTrue(sample >= 0 && sample <= 1000 * dist.getMean());
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0, dist.inverseCumulativeProbability(0));
        assertEquals(1, dist.inverseCumulativeProbability(1.0));
        assertEquals(2, dist.inverseCumulativeProbability(0.6817607165726175));
        assertEquals(3, dist.inverseCumulativeProbability(0.5385317131365868));
        assertEquals(4, dist.inverseCumulativeProbability(0.40040112669955612));
        assertEquals(5, dist.inverseCumulativeProbability(0.26586767046703756));
        assertEquals(6, dist.inverseCumulativeProbability(0.13293383523351878));
        assertEquals(7, dist.inverseCumulativeProbability(0.08824944814483232));
        assertEquals(8, dist.inverseCumulativeProbability(0.06007223192288308));
        assertEquals(9, dist.inverseCumulativeProbability(0.04113733431128547));
        assertEquals(10, dist.inverseCumulativeProbability(0.02942889509510286));
        assertEquals(11, dist.inverseCumulativeProbability(Precision.round(0.02043549213263328, 1)));
    }
}