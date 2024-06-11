package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestWithDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        // Test with empty arrays
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]));
        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[0], null));
        // Test with arrays of different sizes
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 3.5, 4.5};
        assertEquals(6.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.1232, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithCustomConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                MannWhitneyUTest.NaNStrategy.EXCLUDE,
                MannWhitneyUTest.TiesStrategy.LOWER_RANK);

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 3.5, 4.5, Double.NaN};
        Ranking naturalRanking = new NaturalRanking(MannWhitneyUTest.NaNStrategy.EXCLUDE, MannWhitneyUTest.TiesStrategy.LOWER_RANK);

        assertEquals(6.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.0868, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.001);

        x = new double[] {1.0, 2.0, 3.0};
        y = new double[] {1.5, 3.5, 4.5, 3.5, 3.5};
        assertEquals(6.5, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.0373, mannWhitneyUTest.mannnWhitneyUTest(x, y), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[100000], new double[100000]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMannWhitneyUTestWithMismatchedArraySizes() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 3.5};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
}