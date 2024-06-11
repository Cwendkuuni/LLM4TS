import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionTest {

	@Test
	public void testDensity() {
		FDistribution dist = new FDistribution(2, 3);
		double x = 0.5;
		double density = dist.density(x);
		assertEquals(0.7799918679167589, density, 0.000001);

		dist = new FDistribution(5, 7);
		x = 1.2;
		density = dist.density(x);
		assertEquals(0.440346957842777, density, 0.000001);
	}

	@Test
	public void testCumulativeProbability() {
		FDistribution dist = new FDistribution(2, 3);
		double x = 0.5;
		double cumulative = dist.cumulativeProbability(x);
		assertEquals(0.4227494761903706, cumulative, 0.000001);

		dist = new FDistribution(5, 7);
		x = 1.2;
		cumulative = dist.cumulativeProbability(x);
		assertEquals(0.7404776543636027, cumulative, 0.000001);
	}

	@Test
	public void testGetNumeratorDegreesOfFreedom() {
		FDistribution dist = new FDistribution(2, 3);
		double degrees = dist.getNumeratorDegreesOfFreedom();
		assertEquals(2, degrees, 0.0);

		dist = new FDistribution(5, 7);
		degrees = dist.getNumeratorDegreesOfFreedom();
		assertEquals(5, degrees, 0.0);
	}

	@Test
	public void testGetDenominatorDegreesOfFreedom() {
		FDistribution dist = new FDistribution(2, 3);
		double degrees = dist.getDenominatorDegreesOfFreedom();
		assertEquals(3, degrees, 0.0);

		dist = new FDistribution(5, 7);
		degrees = dist.getDenominatorDegreesOfFreedom();
		assertEquals(7, degrees, 0.0);
	}

	@Test
	public void testGetNumericalMean() {
		FDistribution dist = new FDistribution(2, 3);
		double mean = dist.getNumericalMean();
		assertEquals(3.0, mean, 0.0);

		dist = new FDistribution(5, 7);
		mean = dist.getNumericalMean();
		assertEquals(7 / (7 - 2), mean, 0.0);
	}

	@Test
	public void testGetNumericalVariance() {
		FDistribution dist = new FDistribution(2, 3);
		double variance = dist.getNumericalVariance();
		assertEquals(DecimalConstants.TWO.multiply(DecimalConstants.TWO).multiply(5)
				.multiply(1), variance, 0.0); // 40

		dist = new FDistribution(5, 7);
		variance = dist.getNumericalVariance();
		assertEquals(DecimalConstants.TWO.multiply(DecimalConstants.TWO).multiply(12.857142857142858).
				divide(DecimalConstants.FIVE), variance, 0.0); // 10.285714285714286
	}

	@Test
	public void testGetSupportLowerBound() {
		FDistribution dist = new FDistribution(2, 3);
		double lowerBound = dist.getSupportLowerBound();
		assertEquals(0, lowerBound, 0.0);

		dist = new FDistribution(5, 7);
		lowerBound = dist.getSupportLowerBound();
		assertEquals(0, lowerBound, 0.0);
	}

	@Test
	public void testGetSupportUpperBound() {
		FDistribution dist = new FDistribution(2, 3);
		double upperBound = dist.getSupportUpperBound();
		assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);

		dist = new FDistribution(5, 7);
		upperBound = dist.getSupportUpperBound();
		assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
	}

	@Test
	public void testIsSupportLowerBoundInclusive() {
		FDistribution dist = new FDistribution(2, 3);
		boolean inclusive = dist.isSupportLowerBoundInclusive();
		assertTrue(inclusive);

		dist = new FDistribution(5, 7);
		inclusive = dist.isSupportLowerBoundInclusive();
		assertTrue(inclusive);
	}

	@Test
	public void testIsSupportUpperBoundInclusive() {
		FDistribution dist = new FDistribution(2, 3);
		boolean inclusive = dist.isSupportUpperBoundInclusive();
		assertFalse(inclusive);

		dist = new FDistribution(5, 7);
		inclusive = dist.isSupportUpperBoundInclusive();
		assertFalse(inclusive);
	}

	@Test
	public void testIsSupportConnected() {
		FDistribution dist = new FDistribution(2, 3);
		boolean connected = dist.isSupportConnected();
		assertTrue(connected);

		dist = new FDistribution(5, 7);
		connected = dist.isSupportConnected();
		assertTrue(connected);
	}

	@Test(expected=NotStrictlyPositiveException.class)
	public void testInvalidDegreesOfFreedom() {
		new FDistribution(0, 3);
	}

	@Test
	public void testSerializable() {
		FDistribution dist = new FDistribution(2, 3);
		FDistribution deserialized = TestUtils.serializeAndRecover(dist);

		assertEquals(dist.getNumeratorDegreesOfFreedom(), deserialized.getNumeratorDegreesOfFreedom(), 0.0);
		assertEquals(dist.getDenominatorDegreesOfFreedom(), deserialized.getDenominatorDegreesOfFreedom(), 0.0);
		assertTrue(dist.getInverseCumAccuracy() == deserialized.getInverseCumAccuracy());
	}

}