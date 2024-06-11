
package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Assert;
import org.junit.Test;


public class MannWhitneyUTestTest {

	@Test
	public void testMannWhitneyU() {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = {5.0, 6.0, 7.0, 8.0};

		try {
			double result = test.mannWhitneyU(x, y);
			Assert.assertEquals(8.0, result, 0.001);
		} catch (NullArgumentException | NoDataException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testMannWhitneyUTest() {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = {5.0, 6.0, 7.0, 8.0};

		try {
			double result = test.mannWhitneyUTest(x, y);
			Assert.assertEquals(0.0385, result, 0.001);
		} catch (NullArgumentException | NoDataException | ConvergenceException
				| MaxCountExceededException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testEnsureDataConformance() {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = {5.0, 6.0, 7.0, 8.0};

		try {
			test.ensureDataConformance(x, y);
		} catch (NullArgumentException | NoDataException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test(expected = NullArgumentException.class)
	public void testEnsureDataConformanceNullX() throws NullArgumentException, NoDataException {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = null;
		double[] y = {5.0, 6.0, 7.0, 8.0};

		test.ensureDataConformance(x, y);
	}

	@Test(expected = NullArgumentException.class)
	public void testEnsureDataConformanceNullY() throws NullArgumentException, NoDataException {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = null;

		test.ensureDataConformance(x, y);
	}

	@Test(expected = NoDataException.class)
	public void testEnsureDataConformanceEmptyX() throws NullArgumentException, NoDataException {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {};
		double[] y = {5.0, 6.0, 7.0, 8.0};

		test.ensureDataConformance(x, y);
	}

	@Test(expected = NoDataException.class)
	public void testEnsureDataConformanceEmptyY() throws NullArgumentException, NoDataException {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = {};

		test.ensureDataConformance(x, y);
	}

	@Test
	public void testConcatenateSamples() {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double[] x = {1.0, 2.0, 3.0, 4.0};
		double[] y = {5.0, 6.0, 7.0, 8.0};

		double[] result = test.concatenateSamples(x, y);
		double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};

		Assert.assertArrayEquals(expected, result, 0.001);
	}

	@Test
	public void testCalculateAsymptoticPValue() {
		MannWhitneyUTest test = new MannWhitneyUTest();
		double Umin = 8.0;
		int n1 = 4;
		int n2 = 4;

		try {
			double result = test.calculateAsymptoticPValue(Umin, n1, n2);
			Assert.assertEquals(0.0385, result, 0.001);
		} catch (ConvergenceException | MaxCountExceededException e) {
			Assert.fail(e.getMessage());
		}
	}


}

