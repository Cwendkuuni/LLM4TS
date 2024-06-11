package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyUTestWithNullArguments() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, null));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(new double[0], null));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, new double[0]));
    }

    @Test
    public void testMannWhitneyUTestWithNoData() {
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};

        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTestWithOneElementEach() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {5.0};
        double[] y = {3.0};

        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.0, result, 0.001);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testMannWhitneyUTestWithSameArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {5.0, 6.0, 7.0};
        double[] y = {5.0, 6.0, 7.0};

        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.0, result, 0.001);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testMannWhitneyUTestWithDifferentLengths() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};

        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(12.0, result, 0.001);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testMannWhitneyUTestWithTies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 3.0, 3.0, 3.0, 3.0, 5.0};

        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(8.0, result, 0.001);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testMannWhitneyUTestWithTiesAndCustomRanking() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.MIN, TiesStrategy.MIN);

        double[] x = {1.0, 2.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 3.0, 3.0, 3.0, 3.0, 5.0};

        try {
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(7.5, result, 0.001);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testAsymptoticPValueWithNullArguments() {
        assertThrows(NullArgumentException.class, () -> MannWhitneyUTest.calculateAsymptoticPValue(0.0, 0, 0));
    }

    @Test
    public void testAsymptoticPValueWithNoData() {
        assertThrows(NoDataException.class, () -> MannWhitneyUTest.calculateAsymptoticPValue(0.0, 0, 1));
    }

    @Test
    public void testAsymptoticPValueWithUminGreaterThanMax() {
        try {
            double result = MannWhitneyUTest.calculateAsymptoticPValue(10.0, 1, 1);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxCountExceededException e) {
            // Expected
        } catch (NoDataException e) {
            fail("Should not reach here");
        }
    }

    @Test
    public void testAsymptoticPValueWithSmallSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};

        try {
            double Umin = mannWhitneyUTest.mannWhitneyU(x, y);
            double result = MannWhitneyUTest.calculateAsymptoticPValue(Umin, x.length, y.length);
            assertEquals(0.25, result, 0.05);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }

    @Test
    public void testAsymptoticPValueWithLargeSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[10000];
        double[] y = new double[10000];

        for (int i = 0; i < x.length; i++) {
            x[i] = FastMath.random();
            y[i] = FastMath.random();
        }

        try {
            double Umin = mannWhitneyUTest.mannWhitneyU(x, y);
            double result = MannWhitneyUTest.calculateAsymptoticPValue(Umin, x.length, y.length);
            assertEquals(0.05, result, 0.01);
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            // This should not happen
            e.printStackTrace();
        }
    }
}