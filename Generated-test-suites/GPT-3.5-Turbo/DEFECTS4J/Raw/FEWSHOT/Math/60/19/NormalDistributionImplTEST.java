import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Test;

public class NormalDistributionImplTest {

    // Constructor tests

    @Test
    public void testConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertNotNull(distribution);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, -1);
    }

    // Getter tests

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    // Method tests

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0.0001);
        assertEquals(0.24197072451914337, distribution.density(1), 0.0001);
    }

    @Test
    public void testDensity_withNegativeValue() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(-1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
        assertEquals(0.15865525393145705, distribution.cumulativeProbability(-1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(-0.6744897501960817, distribution.inverseCumulativeProbability(0.25), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability_withExtremeValues() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_withInvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(2);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double value = distribution.sample();
        assertNotNull(value);
    }

    // Domain bound tests

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0), 0);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getDomainUpperBound(0.5), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0), 0);
        assertEquals(1, distribution.getInitialDomain(1), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
    }

}