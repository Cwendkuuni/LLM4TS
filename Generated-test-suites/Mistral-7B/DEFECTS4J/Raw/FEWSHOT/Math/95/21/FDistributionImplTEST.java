package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FGammaDistribution;
import org.apache.commons.math3.special.Beta;

import java.util.Locale;

public class FDistributionImplTest {

    /** The default epsilon for comparison */
    private static final double DEFAULT_EPSILON = 1e-12;

    /** The test F distribution */
    private FDistributionImpl fDist;

    /**
     * Setup test
     * @throws MathException if initialization fails
     */
    @org.junit.Before
    public void setUp() throws MathException {
        fDist = new FDistributionImpl(3.0, 5.0);
    }

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), DEFAULT_EPSILON);
        assertEquals(5.0, fDist.getDenominatorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    /**
     * Test cumulative probability
     * @throws MathException if the cumulative probability can not be computed
     */
    @org.junit.Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDist.cumulativeProbability(0.0), DEFAULT_EPSILON);
        assertEquals(0.5, fDist.cumulativeProbability(0.5), DEFAULT_EPSILON);
        assertEquals(1.0, fDist.cumulativeProbability(Double.POSITIVE_INFINITY), DEFAULT_EPSILON);

        assertEquals(0.0476185721931335, fDist.cumulativeProbability(1.5), DEFAULT_EPSILON);
        assertEquals(0.952529228033388, fDist.cumulativeProbability(2.5), DEFAULT_EPSILON);
    }

    /**
     * Test inverse cumulative probability
     * @throws MathException if the inverse cumulative probability can not be computed
     */
    @org.junit.Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(1.5, fDist.inverseCumulativeProbability(0.0476185721931335), DEFAULT_EPSILON);
        assertEquals(2.5, fDist.inverseCumulativeProbability(0.952529228033388), DEFAULT_EPSILON);

        assertThrows(IllegalArgumentException.class, () -> fDist.inverseCumulativeProbability(2.51));
    }

    /**
     * Test getDomainLowerBound
     */
    @org.junit.Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), DEFAULT_EPSILON);
    }

    /**
     * Test getDomainUpperBound
     */
    @org.junit.Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fDist.getDomainUpperBound(0.5), DEFAULT_EPSILON);
    }

    /**
     * Test getInitialDomain
     */
    @org.junit.Test
    public void testGetInitialDomain() {
        assertEquals(6.0 / 4.0, fDist.getInitialDomain(0.5), DEFAULT_EPSILON);
    }

    /**
     * Test setNumeratorDegreesOfFreedom
     */
    @org.junit.Test
    public void testSetNumeratorDegreesOfFreedom() {
        fDist.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDist.getNumeratorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    /**
     * Test getNumeratorDegreesOfFreedom
     */
    @org.junit.Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    /**
     * Test setDenominatorDegreesOfFreedom
     */
    @org.junit.Test
    public void testSetDenominatorDegreesOfFreedom() {
        fDist.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, fDist.getDenominatorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    /**
     * Test getDenominatorDegreesOfFreedom
     */
    @org.junit.Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(5.0, fDist.getDenominatorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    /**
     * Test distribution functions for F and F-Gamma distributions
     * @throws MathException if the cumulative probability can not be computed
     */
    @org.junit.Test
    public void testDistributionFunctions() throws MathException {
        FDistribution fDist1 = new FDistributionImpl(2.0, 4.0);
        FGammaDistribution fGammaDist = new FGammaDistribution(2.5, 0.5);

        for (double x = 0.1; x <= 3.0; x += 0.1) {
            double fDistValue = fDist1.cumulativeProbability(x);
            double fGammaDistValue = fGammaDist.cumulativeProbability(x);
            assertEquals(fDistValue, fGammaDistValue, DEFAULT_EPSILON);
        }
    }

    /**
     * Test set parameters from String
     * @throws MathException if the cumulative probability can not be computed
     */
    @org.junit.Test
    public void testSetParametersFromString() throws MathException {
        String parameters = String.format(Locale.US, "3,5");
        fDist.setParameters(parameters.split(","));

        assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), DEFAULT_EPSILON);
        assertEquals(5.0, fDist.getDenominatorDegreesOfFreedom(), DEFAULT_EPSILON);
    }
}