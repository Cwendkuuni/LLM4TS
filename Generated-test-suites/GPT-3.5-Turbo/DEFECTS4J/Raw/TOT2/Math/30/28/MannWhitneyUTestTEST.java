package org.apache.commons.math3.stat.inference;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setup() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testEnsureDataConformance_NullArgumentException() {
        try {
            mannWhitneyUTest.mannWhitneyU(null, new double[]{1, 2, 3});
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullArgumentException);
        }
    }

    @Test
    public void testEnsureDataConformance_NoDataException() {
        try {
            mannWhitneyUTest.mannWhitneyU(new double[]{}, new double[]{1, 2, 3});
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoDataException);
        }
    }

    @Test
    public void testMannWhitneyU_NormalCase() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double value = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(9.0, value, 0.0001);
    }

    @Test
    public void testCalculateAsymptoticPValue_NormalCase() {
        double value = mannWhitneyUTest.calculateAsymptoticPValue(6, 3, 3);
        Assert.assertEquals(0.496, value, 0.001);
    }

    @Test
    public void testMannWhitneyUTest_NormalCase() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.496, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTest_ExceptionHandling() {
        try {
            mannWhitneyUTest.mannWhitneyUTest(null, new double[]{1, 2, 3});
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullArgumentException);
        }
    }

    @Test
    public void testMannWhitneyUTest_EdgeCases() {
        double[] x = {1, 2, 3};
        double[] y = {1, 2, 3};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(1.0, pValue, 0.001);
    }
}