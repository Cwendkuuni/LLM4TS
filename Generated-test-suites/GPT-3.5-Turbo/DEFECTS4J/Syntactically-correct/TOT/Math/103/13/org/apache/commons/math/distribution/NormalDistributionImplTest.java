import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double expected = 5;
        double actual = distribution.getMean();
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(7);
        double expected = 7;
        double actual = distribution.getMean();
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double expected = 2;
        double actual = distribution.getStandardDeviation();
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        double expected = 3;
        double actual = distribution.getStandardDeviation();
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double x = 0;
        double expected = 0.5;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double expected = 0;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroP() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0;
        double expected = Double.NEGATIVE_INFINITY;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneP() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 1;
        double expected = Double.POSITIVE_INFINITY;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-3);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double expected = -Double.MAX_VALUE;
        double actual = distribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0.75;
        double expected = Double.MAX_VALUE;
        double actual = distribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double expected = 1;
        double actual = distribution.getInitialDomain(p);
        assertEquals(expected, actual, 0);
    }
}