package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import java.util.Arrays;

public class MannWhitneyUTestCase {

    @Test
    public void testCreateMannWhitneyUTestInstance() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.FIXED, mannWhitneyUTest.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getTiesStrategy());

        MannWhitneyUTest mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(
                NaNStrategy.SAFE, TiesStrategy.LOW);
        assertEquals(NanStrategy.SAFE, mannWhititneyUTestWithCustomStrategies.getNanStrategy());
        assertEquals(TiesStrategy.LOW, mannWhitneyUTestWithCustomStrategies.getTiesStrategy());
    }

    @Test
    public void testManWhitneyU_EmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]));
    }

    @Test
    public void testManWhitneyU_IdenticalArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0};
        double[] y = {1.0, 1.5, 2.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.0);
    }

    @Test
    public void testManWhitneyU_DifferentLengthArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0};
        double[] y = {1.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 0.0);
    }

    @Test
    public void testManWhitneyU_NullArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(null, null));
    }

    @Test
    public void testManWhitneyUTest_EmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testManWhitneyUTest_IdenticalArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0};
        double[] y = {1.0, 1.5, 2.0};

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 0.01);
    }

    @Test
    public void testManWhitneyUTest_DifferentLengthArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0};
        double[] y = {1.0};

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        // The exact p-value depends on the randomization, but it should be close to 0.5
        double expectedPValue = 0.5;
        assertEquals(expectedPValue, pValue, 0.05);
    }

    @Test
    public void testManWhitneyUTest_NullArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(null, null));
    }
}