package org.apache.commons.math.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.0);
        double[] probabilities = new double[10];
        for (int i = 0; i < 10; i++) {
            probabilities[i] = dist.probability(i);
        }
        assertArrayEquals(new double[]{0.135335283230631, 0.210223655914285, 0.214394146527574, 0.182133231707561, 0.145926211172222, 0.106140542787613, 0.0718375473535526, 0.045917283236186, 0.029158122312458, 0.018213323170756}, probabilities, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.0);
        double[] cdfValues = new double[11];
        for (int i = 0; i < 11; i++) {
            cdfValues[i] = dist.cumulativeProbability(i);
        }
        assertArrayEquals(new double[]{0.0, 0.135335283230631, 0.250670566461258, 0.385987556692869, 0.513284741233322, 0.634104931907293, 0.747251131139412, 0.846020884251994, 0.931341828293438, 0.989422828229275, 1.0}, cdfValues, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.0);
        double[] normApproxProbabilities = new double[11];
        for (int i = 0; i < 11; i++) {
            normApproxProbabilities[i] = dist.normalApproximateProbability(i);
        }
        assertArrayEquals(cdfValues, normApproxProbabilities, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.0);
        int size = 1000;
        int[] samples = new int[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            samples[i] = dist.sample();
        }
        int sum = 0;
        for (int s : samples) {
            sum += s;
        }
        assertEquals(2000, sum);
    }

    @Test
    public void testPersistence() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.0);
        PoissonDistribution distRoundTrip = (PoissonDistribution) PoissonDistribution.distributionInstance(dist.getParameters());
        assertEquals(dist.probability(0), distRoundTrip.probability(0), 0.001);
        assertEquals(dist.cumulativeProbability(0), distRoundTrip.cumulativeProbability(0), 0.001);
        assertEquals(dist.mean(), distRoundTrip.mean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }
}