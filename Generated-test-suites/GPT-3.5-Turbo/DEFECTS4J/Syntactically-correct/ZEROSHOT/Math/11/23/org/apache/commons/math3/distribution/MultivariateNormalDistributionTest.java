import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {

	@Test
	public void testConstructor() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

		try {
			MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

			assertNotNull(distribution);
			assertArrayEquals(means, distribution.getMeans(), 0.01);
			assertTrue(distribution.getCovariances().getData() == covariances);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testConstructorWithInvalidMeans() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testConstructorWithInvalidCovariances() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
	}

	@Test(expected = SingularMatrixException.class)
	public void testConstructorWithSingularCovarianceMatrix() {
		double[] means = {1.0, 2.0};
		double[][] covariances = {{1.0, 1.0}, {2.0, 2.0}};

		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
	}

	@Test(expected = NonPositiveDefiniteMatrixException.class)
	public void testConstructorWithNonPositiveDefiniteCovarianceMatrix() {
		double[] means = {1.0, 2.0};
		double[][] covariances = {{-1.0, 0.0}, {0.0, 1.0}};

		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
	}

	@Test
	public void testDensity() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
		double[] values = {1.5, 2.5, 3.5};

		try {
			MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

			double density = distribution.density(values);

			assertEquals(0.06349363593424098, density, 0.01);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetStandardDeviations() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

		try {
			MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

			double[] stdDeviations = distribution.getStandardDeviations();

			assertArrayEquals(new double[]{1.0, 1.0, 1.0}, stdDeviations, 0.01);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSample() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

		try {
			MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

			double[] sample = distribution.sample();

			assertNotNull(sample);
			assertEquals(3, sample.length);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetExponentTerm() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
		double[] values = {1.5, 2.5, 3.5};

		try {
			MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

			double exponentTerm = distribution.getExponentTerm(values);

			assertEquals(0.2981972841637191, exponentTerm, 0.01);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}