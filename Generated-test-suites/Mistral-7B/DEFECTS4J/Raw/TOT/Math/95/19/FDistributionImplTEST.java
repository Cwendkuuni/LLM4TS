package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    private FDistribution distribution;

    @Before
    public void setUp() {
        this.distribution = new FDistributionImpl(1.5, 2.0);
    }

    /**
     * Test {@link FDistributionImpl#FDistributionImpl(double, double)} with
     * valid arguments.
     */
    @Test
    public void testValidConstruction() {
        assertEquals(1.5, this.distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, this.distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test {@link FDistributionImpl#cumulativeProbability(double)} with valid
     * arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArguments() throws MathException {
        double x = 1.5;
        double expected = Beta.regularizedBeta((1.5 * x) / (2.0 + 1.5 * x),
            0.5 * 1.5, 0.5 * 2.0);
        double actual = this.distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#cumulativeProbability(double)} with an edge
     * case (x = 0).
     */
    @Test
    public void testCumulativeProbabilityEdgeCase() throws MathException {
        double x = 0.0;
        double expected = 0.0;
        double actual = this.distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#cumulativeProbability(double)} with an error
     * case (x < 0).
     */
    @Test
    public void testCumulativeProbabilityErrorCase() {
        double x = -1.0;
        assertThrows(MathException.class, () -> {
            this.distribution.cumulativeProbability(x);
        });
    }

    /**
     * Test {@link FDistributionImpl#inverseCumulativeProbability(double)} with
     * valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityValidArguments() throws MathException {
        double p = 0.5;
        double expected = 1.053747419412663;
        double actual = this.distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#inverseCumulativeProbability(double)} with an
     * edge case (p = 0).
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCase() throws MathException {
        double p = 0.0;
        double expected = 0.0;
        double actual = this.distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#inverseCumulativeProbability(double)} with an
     * error case (p < 0 or p > 1).
     */
    @Test
    public void testInverseCumulativeProbabilityErrorCases() {
        double p = -1.0;
        assertThrows(IllegalArgumentException.class, () -> {
            this.distribution.inverseCumulativeProbability(p);
        });

        p = 1.1;
        assertThrows(MathException.class, () -> {
            this.distribution.inverseCumulativeProbability(p);
        });
    }

    /**
     * Test {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} with
     * valid arguments.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidArguments() {
        double degreesOfFreedom = 3.0;
        this.distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, this.distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} with
     * invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidArguments() {
        this.distribution.setNumeratorDegreesOfFreedom(-1.0);
    }
}