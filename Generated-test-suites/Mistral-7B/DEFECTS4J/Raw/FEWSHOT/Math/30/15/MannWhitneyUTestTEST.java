package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.0);

        x = new double[0];
        y = new double[0];
        try {
            mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }

        x = new double[]{1.0};
        y = new double[]{2.0};
        try {
            mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
            fail();
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};

        double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(0.125, pValue, 0.01);

        x = new double[]{1.0};
        y = new double[]{2.0};
        try {
            pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail();
        } catch (NoDataException e) {
            // Expected
        }

        x = new double[0];
        y = new double[0];
        try {
            pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}