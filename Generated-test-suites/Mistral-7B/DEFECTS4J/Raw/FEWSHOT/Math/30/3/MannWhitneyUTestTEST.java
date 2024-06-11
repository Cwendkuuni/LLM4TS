package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.commons.math3.distribution.NormalDistribution;
import org.apache.commons.commons.math3.exception.ConvergenceException;
import org.apache.commons.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.commons.math3.exception.NoDataException;
import org.apache.commons.commons.math3.exception.NullArgumentException;
import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        assertEquals(3.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.015, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.001);

        MannWhitneyUTest mannWhitneyUTestNaN = new MannWhitneyUTest(NaturalRanking.FIXED, TiesStrategy.MIN);
        double[] xNaN = {1.0, Double.NaN, 3.0};
        double[] yNaN = {1.5, 2.5, Double.NaN};
        assertThrows(NoDataException.class, () -> mannWhitneyUTestNaN.mannnWhitneyUTest(xNaN, yNaN));
    }

    @Test
    public void testMannWhitneyUTestWithCustomRankingStrategy() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.DENSE_RANK, TiesStrategy.MIN);
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0, 4.0};
        double[] y = {2.5, 2.5, 2.5, 3.5, 4.5, 5.5};
        assertEquals(5.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.05, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannnWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTestWithNullArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannnWhitneyUTest(null, null));
    }

    @Test
    public void testMannWhitneyUTestWithLargeArrays() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[1000000];
        double[] y = new double[1000000];
        for (int i = 0; i < x.length; i++) {
            x[i] = i + 0.5;
            y[i] = i;
        }
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        assertEquals(mannWhitneyU, mannWhitneyUTestResult, 0.01);
    }
}