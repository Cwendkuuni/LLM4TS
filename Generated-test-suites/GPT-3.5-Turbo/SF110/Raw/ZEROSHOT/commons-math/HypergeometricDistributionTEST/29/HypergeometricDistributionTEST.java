package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private RandomGenerator rng;
    private HypergeometricDistribution hypergeometricDistribution;

    @Before
    public void setUp() throws Exception {
        rng = new Well19937c();
        hypergeometricDistribution = new HypergeometricDistribution(rng, 100, 50, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() throws Exception {
        new HypergeometricDistribution(rng, 0, 50, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_ThrowsNotPositiveException() throws Exception {
        new HypergeometricDistribution(rng, 100, -1, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_ThrowsNotPositiveException_2() throws Exception {
        new HypergeometricDistribution(rng, 100, 50, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ThrowsNumberIsTooLargeException() throws Exception {
        new HypergeometricDistribution(rng, 100, 101, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ThrowsNumberIsTooLargeException_2() throws Exception {
        new HypergeometricDistribution(rng, 100, 50, 101);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(10), 0);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(11), 0);
    }

    @Test
    public void testGetLowerDomain() {
        assertEquals(5, hypergeometricDistribution.getLowerDomain(100, 50, 10));
    }

    @Test
    public void testGetUpperDomain() {
        assertEquals(10, hypergeometricDistribution.getUpperDomain(50, 10));
    }

    @Test
    public void testProbability() {
        assertEquals(0.0032206110199442, hypergeometricDistribution.probability(8), 0);
        assertEquals(0.0013394901070454, hypergeometricDistribution.probability(9), 0);
        assertEquals(0.0003630888931649, hypergeometricDistribution.probability(10), 0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(10), 0);
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(9), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(5.0, hypergeometricDistribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.6666666666666667, hypergeometricDistribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(45, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

}