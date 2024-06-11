package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testInstanceCreation() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);

        MannWhitneyUTest mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(
                NaNStrategy.SKIP, TiesStrategy.DENSE_RANK);
        assertNotNull(mannWhitneyUTestWithCustomStrategies);
    }

    @Test
    public void testEnsureDataConformance() {
        double[] x = new double[0];
        double[] y = new double[0];

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(null, y));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(x, null));
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(null, null));

        // Valid test case
        double[] xValid = {1.0, 2.0, 3.0};
        double[] yValid = {4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyU(xValid, yValid);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 4.0, 4.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.001);

        x = new double[]{1.0, 1.5, 2.0};
        y = new double[]{2.5, 3.0, 3.5};

        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.5, mannWhitneyU, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 4.0, 4.0};

        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        assertEquals(0.5, pValue, 0.001);

        x = new double[]{1.0, 1.5, 2.0};
        y = new double[]{2.5, 3.0, 3.5};

        pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        assertEquals(0.125, pValue, 0.001);
    }
}