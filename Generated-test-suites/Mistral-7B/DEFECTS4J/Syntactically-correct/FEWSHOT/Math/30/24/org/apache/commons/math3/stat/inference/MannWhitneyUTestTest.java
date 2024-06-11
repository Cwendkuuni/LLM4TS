package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.commons.math3.distribution.NormalDistribution;
import org.apache.commons.commons.math3.exception.ConvergenceException;
import org.apache.commons.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.commons.math3.exception.NoDataException;
import org.apache.commons.commons.math3.exception.NullArgumentException;
import org.apache.commons.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.commons.math3.util.FastMath;
import org.junit.Test;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestInstanceCreation() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        MannWhitneyUTest testWithStrategies = new MannWhitneyUTest(
                NANStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    @Test
    public void testMannWhitneyUEmptyArrays() {
        assertThrows(NoDataException.class, () -> MannWhitneyUTest.mannWhitneyU(
                new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUNullArguments() {
        assertThrows(NullArgumentException.class, () -> MannWhitneyUTest.mannWhitneyU(
                null, new double[0]));
        assertThrows(NullArgumentException.class, () -> MannWhitneyUTest.mannWhitneyU(
                new double[0], null));
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.1, 2.2, 3.3, 4.4};
        double[] y = {1.5, 2.6, 3.5, 4.5, 5.5};

        assertEquals(5.0, MannWhitneyUTest.mannWhitneyU(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        double[] x = {1.1, 2.2, 3.3, 4.4};
        double[] y = {1.5, 2.6, 3.5, 4.5, 5.5};

        double pValue = MannWhitneyUTest.mannWhitneyUTest(x, y);

        assertEquals(0.1, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTestNaN() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        double[] x = {1.1, 2.2, 3.3, Double.NaN};
        double[] y = {1.5, 2.6, 3.5, 4.5, 5.5};

        assertThrows(ConvergenceException.class, () -> MannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTestZeroLength() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        double[] x = {};
        double[] y = {};

        assertThrows(NoDataException.class, () -> MannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testCalculateAsymptoticPValue() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        double Umin = 2.5;
        int n1 = 3;
        int n2 = 5;

        assertEquals(0.334, MannWhitneyUTest.calculateAsymptoticPValue(Umin, n1,
                n2), 0.001);
    }

}