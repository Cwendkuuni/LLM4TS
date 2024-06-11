// The JUnit test file starts here
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

// Test class for FDistribution
public class FDistributionTest {

// Test constructor with valid parameters.
@Test
public void testConstructorValid() throws NotStrictlyPositiveException {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
}

// Test constructor with invalid parameters.
@Test(expected = NotStrictlyPositiveException.class)
public void testConstructorInvalid() {
new FDistribution(-1.0, 1.0);
}

// Test constructor with random number generator, valid parameters.
@Test
public void testConstructorRandomGeneratorValid() throws NotStrictlyPositiveException {
RandomGenerator rng = new Well19937c();
FDistribution dist = new FDistribution(rng, 2.5, 6.5);
assertEquals(2.5, dist.getNumeratorDegreesOfFreedom(), 0.0);
assertEquals(6.5, dist.getDenominatorDegreesOfFreedom(), 0.0);
}

// Test constructor with random number generator, invalid parameters.
@Test(expected = NotStrictlyPositiveException.class)
public void testConstructorRandomGeneratorInvalid() {
RandomGenerator rng = new Well19937c();
new FDistribution(rng, -1.0, 1.0);
}

// Test getNumeratorDegreesOfFreedom().
@Test
public void testGetNumeratorDegreesOfFreedom() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
}

// Test getDenominatorDegreesOfFreedom().
@Test
public void testGetDenominatorDegreesOfFreedom() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
}

// Test getNumericalMean().
@Test
public void testGetNumericalMean() {
FDistribution dist = new FDistribution(4.0, 5.0);
assertEquals(5.0 / 3.0, dist.getNumericalMean(), 1e-10);
}

// Test getNumericalMean() with invalid denominator degrees of freedom.
@Test
public void testGetNumericalMeanInvalid() {
FDistribution dist = new FDistribution(3.0, 2.0);
assertEquals(Double.NaN, dist.getNumericalMean(), 0.0);
}

// Test getNumericalVariance().
@Test
public void testGetNumericalVariance() {
FDistribution dist = new FDistribution(5.0, 7.0);
assertEquals(14.0 / 15.0, dist.getNumericalVariance(), 1e-10);
}

// Test getNumericalVariance() with invalid denominator degrees of freedom.
@Test
public void testGetNumericalVarianceInvalid() {
FDistribution dist = new FDistribution(3.0, 3.0);
assertEquals(Double.NaN, dist.getNumericalVariance(), 0.0);
}

// Test cumulativeProbability() with small values.
@Test
public void testCumulativeProbabilitySmall() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-10);
assertEquals(0.02872948, dist.cumulativeProbability(0.1), 1e-5);
assertEquals(0.5, dist.cumulativeProbability(1.0), 1e-10);
}

// Test cumulativeProbability() with large values.
@Test
public void testCumulativeProbabilityLarge() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-10);
}

// Test density() with small values.
@Test
public void testDensitySmall() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(0.0, dist.density(0.0), 1e-10);
assertEquals(0.01238312, dist.density(0.1), 1e-5);
}

// Test density() with large values.
@Test
public void testDensityLarge() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertEquals(0.0, dist.density(Double.POSITIVE_INFINITY), 1e-10);
}

// Test isSupportLowerBoundInclusive().
@Test
public void testIsSupportLowerBoundInclusive() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertTrue(dist.isSupportLowerBoundInclusive());
}

// Test isSupportUpperBoundInclusive().
@Test
public void testIsSupportUpperBoundInclusive() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertFalse(dist.isSupportUpperBoundInclusive());
}

// Test isSupportConnected().
@Test
public void testIsSupportConnected() {
FDistribution dist = new FDistribution(3.0, 5.0);
assertTrue(dist.isSupportConnected());
}

// The JUnit test file ends here
}