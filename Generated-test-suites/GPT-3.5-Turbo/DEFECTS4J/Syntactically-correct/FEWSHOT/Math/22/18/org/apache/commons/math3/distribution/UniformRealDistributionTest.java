package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
	private UniformRealDistribution distribution;
	private final double EPSILON = 1e-9;

	@Before
	public void setup() {
		distribution = new UniformRealDistribution();
	}

	@Test
	public void testDensity() {
		assertEquals(1.0, distribution.density(0.5), EPSILON);
		assertEquals(0.0, distribution.density(1.5), EPSILON);
		assertEquals(0.0, distribution.density(-0.5), EPSILON);
	}

	@Test
	public void testCumulativeProbability() {
		assertEquals(0.0, distribution.cumulativeProbability(-0.5), EPSILON);
		assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
		assertEquals(1.0, distribution.cumulativeProbability(1.5), EPSILON);
	}

	@Test
	public void testGetNumericalMean() {
		assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
	}

	@Test
	public void testGetNumericalVariance() {
		assertEquals(0.08333333333333334, distribution.getNumericalVariance(), EPSILON);
	}

	@Test
	public void testGetSupportLowerBound() {
		assertEquals(0.0, distribution.getSupportLowerBound(), EPSILON);
	}

	@Test
	public void testGetSupportUpperBound() {
		assertEquals(1.0, distribution.getSupportUpperBound(), EPSILON);
	}

	@Test
	public void testIsSupportLowerBoundInclusive() {
		assertTrue(distribution.isSupportLowerBoundInclusive());
	}

	@Test
	public void testIsSupportUpperBoundInclusive() {
		assertTrue(!distribution.isSupportUpperBoundInclusive());
	}

	@Test
	public void testIsSupportConnected() {
		assertTrue(distribution.isSupportConnected());
	}

	@Test
	public void testSample() {
		double sample = distribution.sample();
		assertTrue(sample >= 0.0 && sample <= 1.0);
	}
}