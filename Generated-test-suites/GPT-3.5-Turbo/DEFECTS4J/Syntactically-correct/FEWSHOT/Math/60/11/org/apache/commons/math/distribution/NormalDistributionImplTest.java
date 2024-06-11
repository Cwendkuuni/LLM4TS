package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(0, normalDistribution.getMean(), 0);
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testConstructorWithInvalidStandardDeviation() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0, -1));
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), 0);
        assertEquals(0.05399096651318806, normalDistribution.density(-1), 0);
        assertEquals(0.24197072451914337, normalDistribution.density(1), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
        assertEquals(0.8413447460685429, normalDistribution.cumulativeProbability(1), 0);
        assertEquals(1, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(-0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.25), 0);
        assertEquals(1.959963984540054, normalDistribution.inverseCumulativeProbability(0.975), 0);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testSample() throws MathException {
        assertThrows(MaxIterationsExceededException.class, () -> normalDistribution.sample());
    }

}