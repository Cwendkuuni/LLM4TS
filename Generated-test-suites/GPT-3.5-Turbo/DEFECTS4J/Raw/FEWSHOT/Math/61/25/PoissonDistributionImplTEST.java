package org.apache.commons.math.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5, 1E-6, 100000);
        assertEquals(0.05987442781070094, dist.probability(0), 0.0);
        assertEquals(0.22404180765538775, dist.probability(1), 0.0);
        assertEquals(0.2240418076553877, dist.probability(2), 0.0);
        assertEquals(0.16803135574154074, dist.probability(3), 0.0);
        assertEquals(0.10081881344492445, dist.probability(4), 0.0);
        assertEquals(0.05040940672246222, dist.probability(5), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5, 1E-6, 100000);
        assertEquals(0.2231301601484298, dist.cumulativeProbability(0), 0.0);
        assertEquals(0.4478915137291816, dist.cumulativeProbability(1), 0.0);
        assertEquals(0.6710801358823693, dist.cumulativeProbability(2), 0.0);
        assertEquals(0.8399104916249105, dist.cumulativeProbability(3), 0.0);
        assertEquals(0.9407299607560135, dist.cumulativeProbability(4), 0.0);
        assertEquals(0.9772001925321371, dist.cumulativeProbability(5), 0.0);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5, 1E-6, 100000);
        assertEquals(0.20211558266101183, dist.normalApproximateProbability(0), 0.0);
        assertEquals(0.579259709439103, dist.normalApproximateProbability(1), 0.0);
        assertEquals(0.7853902304655975, dist.normalApproximateProbability(2), 0.0);
        assertEquals(0.9014426523970514, dist.normalApproximateProbability(3), 0.0);
        assertEquals(0.9599091258768773, dist.normalApproximateProbability(4), 0.0);
        assertEquals(0.9837159480751095, dist.normalApproximateProbability(5), 0.0);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5, 1E-6, 100000);
        int sample1 = dist.sample();
        int sample2 = dist.sample();
        int sample3 = dist.sample();

        assertTrue(sample1 >= 0 && sample1 <= Integer.MAX_VALUE);
        assertTrue(sample2 >= 0 && sample2 <= Integer.MAX_VALUE);
        assertTrue(sample3 >= 0 && sample3 <= Integer.MAX_VALUE);
    }
}