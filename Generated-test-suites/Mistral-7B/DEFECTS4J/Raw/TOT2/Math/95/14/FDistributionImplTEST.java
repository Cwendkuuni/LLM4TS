package org.junit.tests;

import static org.junit.Assert.*;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;

public class FDistributionImplTest {

// Test for cumulativeProbability
@Test
public void testCumulativeProbability() throws Exception {
    FDistributionImpl dist = new FDistributionImpl(1.0, 2.0);
    double x = 1.5;
    double expectedProbability = Beta.regularizedBeta((1.0 * x) / (2.0 + 1.0 * x), 0.5, 0.5);
    double actualProbability = dist.cumulativeProbability(x);
    assertEquals(expectedProbability, actualProbability, 0.001);
}

// Test for negative degrees of freedom
@Test(expected = IllegalArgumentException.class)
public void testNegativeNumeratorDegreesOfFreedom() {
    new FDistributionImpl(-1.0, 2.0);
}

// Test for zero numerator degrees of freedom
@Test(expected = IllegalArgumentException.class)
public void testZeroNumeratorDegreesOfFreedom() {
    new FDistributionImpl(0.0, 2.0);
}

// Test for zero denominator degrees of freedom
@Test(expected = IllegalArgumentException.class)
public void testZeroDenominatorDegreesOfFreedom() {
    new FDistributionImpl(1.0, 0.0);
}

// Test for large degrees of freedom
@Test
public void testLargeDegreesOfFreedom() {
    FDistributionImpl dist = new FDistributionImpl(10000.0, 10000.0);
    double x = 1.5;
    double expectedProbability = Beta.regularizedBeta((10000.0 * x) / (10000.0 + 10000.0 * x), 5000.5, 5000.5);
    double actualProbability = dist.cumulativeProbability(x);
    assertEquals(expectedProbability, actualProbability, 0.001);
}

// Test for out-of-range x values
@Test(expected = MathException.class)
public void testOutOfRangeXValues() {
    FDistributionImpl dist = new FDistributionImpl(1.0, 2.0);
    double x = 1000.0;
    dist.cumulativeProbability(x);
}

// Test for invalid probability values
@Test(expected = IllegalArgumentException.class)
public void testInvalidProbabilityValues() {
    FDistributionImpl dist = new FDistributionImpl(1.0, 2.0);
    dist.inverseCumulativeProbability(-0.1);
}

}