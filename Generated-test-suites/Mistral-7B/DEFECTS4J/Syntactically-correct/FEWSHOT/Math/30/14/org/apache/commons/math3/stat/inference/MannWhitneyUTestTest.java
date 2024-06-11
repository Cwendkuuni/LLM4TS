package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Test;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyU_EmptyArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> {
            MannWhitneyUTest.mannWhitneyU(new double[]{}, new double[]{});
        });
    }

    @Test
    public void testMannWhitneyU_NullArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyU(null, new double[]{});
        });

        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyU(new double[]{}, null);
        });
    }

    @Test
    public void testMannWhitneyU_SameArray() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 2.0, 2.0};
        double[] y = x.clone();

        double mannWhitneyU = MannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(Double.POSITIVE_INFINITY, mannWhitneyU);
    }

    @Test
    public void testMannWhitneyU_SingleElementArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0};
        double[] y = new double[]{2.0};

        double mannWhitneyU = MannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(0.0, mannWhitneyU);
    }

    @Test
    public void testMannWhitneyUTest_EmptyArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> {
            MannWhitneyUTest.mannWhitneyUTest(new double[]{}, new double[]{});
        });
    }

    @Test
    public void testMannWhitneyUTest_SameArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 2.0, 2.0};
        double[] y = x.clone();

        assertThrows(IllegalArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyUTest(x, y);
        });
    }

    @Test
    public void testMannWhitneyUTest_NullArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyUTest(null, new double[]{});
        });

        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyUTest(new double[]{}, null);
        });
    }

    @Test
    public void testMannWhitneyUTest_DifferentLengthArrays() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0};
        double[] y = new double[]{2.0, 3.0, 4.0};

        double mannWhitneyUTestResult = MannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.06666666666666667, mannWhitneyUTestResult, 0.0001);

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.MIN, NaNStrategy.REMOVE);
        mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.06666666666666667, mannWhitneyUTestResult, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_DifferentLengthArraysWithNaN() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, Double.NaN, 2.0};
        double[] y = new double[]{2.0, 3.0, 4.0, Double.NaN};

        double mannWhitneyUTestResult = MannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, mannWhitneyUTestResult);

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.MIN, NaNStrategy.KEEP);
        mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, mannWhitneyUTestResult);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 10;
        int n1 = 10;
        int n2 = 15;

        double pValue = MannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.014448035265958, pValue, 0.0001);
    }
}