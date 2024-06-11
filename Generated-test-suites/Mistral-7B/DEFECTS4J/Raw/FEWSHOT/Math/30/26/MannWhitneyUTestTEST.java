package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.Ranking;

import org.junit.Test;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyUTest_NullArguments() {
        assertThrows(NullArgumentException.class, () -> {
            new MannWhitneyUTest().mannWhitneyUTest(null, null);
        });
        assertThrows(NullArgumentException.class, () -> {
            new MannWhitneyUTest().mannWhitneyUTest(new double[0], null);
        });
        assertThrows(NullArgumentException.class, () -> {
            new MannWhitneyUTest().mannWhitneyUTest(null, new double[0]);
        });
    }

    @Test
    public void testMannWhitneyUTest_EmptyArrays() {
        assertThrows(NoDataException.class, () -> {
            new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]);
        });
    }

    @Test
    public void testMannWhitneyUTest_EmptyFirstArray() {
        double[] x = new double[0];
        double[] y = {1.0, 2.0, 3.0};

        try {
            new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_EmptySecondArray() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = new double[0];

        try {
            new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_SimpleValues() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double result = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest_SameValues() {
        double[] x = {1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0};

        try {
            new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException | MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_ValuesWithTies() {
        double[] x = {1.0, 1.5, 1.5, 2.0, 2.5, 2.5, 3.0};
        double[] y = {2.0, 2.0, 2.0, 2.0, 2.5, 3.0, 3.5};

        double result = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(0.048, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest_ValuesWithNaN() {
        double[] x = {1.0, Double.NaN, 3.0};
        double[] y = {Double.NaN, 2.0, 4.0};

        try {
            new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_ValuesWithLongArrays() {
        double[] x = new double[10000];
        double[] y = new double[10000];

        for (int i = 0; i < x.length; i++) {
            x[i] = i + 1;
            y[i] = i + 2;
        }

        double result = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 0.1;
        int n1 = 10;
        int n2 = 20;

        double result = new MannWhitneyUTest().calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.018, result, 0.001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testCalculateAsymptoticPValue_MaxCountExceeded()
            throws ConvergenceException, MaxCountExceededException {
        double Umin = 0.0;
        int n1 = 1;
        int n2 = Integer.MAX_VALUE;

        new MannWhitneyUTest().calculateAsymptoticPValue(Umin, n1, n2);
    }
}