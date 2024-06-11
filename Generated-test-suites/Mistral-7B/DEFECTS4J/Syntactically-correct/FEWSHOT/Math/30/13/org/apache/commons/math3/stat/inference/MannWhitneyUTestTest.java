package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestEmptyArrays() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTestNullArrays() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], null));
    }

    @Test
    public void testMannWhitneyUTestEqualArrays() {
        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = x.clone();

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(0.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.0001);
        assertEquals(1.0, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyUTestDifferentLengthArrays() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(3.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.0001);
        assertEquals(0.3333333333333333, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyUTestWithCustomRanking() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public int nanStrategy(double value) {
                return -1;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int tiesStrategy(int i, int rank, int n) {
                return rank;
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        double[] x = {1.0, 2.0, 2.0, 3.0};
        double[] y = {1.0, 3.0, 3.0, 3.0};

        assertEquals(2.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.0001);
        assertEquals(0.5, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.0001);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptySample() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[]{1.0});
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergence() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {Double.NaN, Double.NaN, Double.NaN};
        double[] y = {1.0, 2.0, 3.0};
        mannWhitneyUTest.mannnWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceeded() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[1000000];
        double[] y = new double[1000000];
        mannWhitneyUTest.mannnWhitneyUTest(x, y);
    }
}