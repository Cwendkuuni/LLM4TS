package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
	
	@Test
	public void testConstructor() {
		try {
			MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(null, null, null);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof NullPointerException);
		}
	}

	@Test
	public void testGetMeans() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[] result = mnd.getMeans();
		Assert.assertArrayEquals(means, result, 0.0001);
	}

	@Test
	public void testGetCovariances() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RealMatrix result = mnd.getCovariances();
		RealMatrix expected = new Array2DRowRealMatrix(covariances);
		
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testDensity() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[] vals = {1.0, 2.0, 3.0};
		double result = mnd.density(vals);
		
		Assert.assertEquals(0.013054084, result, 0.0001);
	}

	@Test
	public void testGetStandardDeviations() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[] result = mnd.getStandardDeviations();
		double[] expected = {1.0, 1.41421356237, 1.73205080757};
		
		Assert.assertArrayEquals(expected, result, 0.0001);
	}

	@Test
	public void testSample() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[] result = mnd.sample();

		Assert.assertEquals(3, result.length);
	}

	@Test
	public void testGetExponentTerm() {
		double[] means = {1.0, 2.0, 3.0};
		double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
		
		MultivariateNormalDistribution mnd = null;
		try {
			mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[] values = {1.0, 2.0, 3.0};
		double result = mnd.getExponentTerm(values);

		Assert.assertEquals(1.0, result, 0.0);
	}

}