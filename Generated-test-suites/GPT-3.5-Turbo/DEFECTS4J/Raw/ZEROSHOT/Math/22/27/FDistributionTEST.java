import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

	@Test
	public void testDensity() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.density(2);
		assertEquals(0.0588235294, result, 0.0000000001);
	}

	@Test
	public void testCumulativeProbability() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.cumulativeProbability(2);
		assertEquals(0.7978559453, result, 0.0000000001);
	}

	@Test
	public void testGetNumericalMean() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.getNumericalMean();
		assertEquals(2.0, result, 0.0000000001);
	}

	@Test
	public void testGetNumericalVariance() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.getNumericalVariance();
		assertEquals(6.0, result, 0.0000000001);
	}

	@Test
	public void testGetSupportLowerBound() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.getSupportLowerBound();
		assertEquals(0.0, result, 0.0);
	}

	@Test
	public void testGetSupportUpperBound() {
		FDistribution dist = new FDistribution(3, 4);
		double result = dist.getSupportUpperBound();
		assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
	}

	@Test
	public void testIsSupportLowerBoundInclusive() {
		FDistribution dist = new FDistribution(3, 4);
		boolean result = dist.isSupportLowerBoundInclusive();
		assertTrue(result);
	}

	@Test
	public void testIsSupportUpperBoundInclusive() {
		FDistribution dist = new FDistribution(3, 4);
		boolean result = dist.isSupportUpperBoundInclusive();
		assertFalse(result);
	}

	@Test
	public void testIsSupportConnected() {
		FDistribution dist = new FDistribution(3, 4);
		boolean result = dist.isSupportConnected();
		assertTrue(result);
	}

}