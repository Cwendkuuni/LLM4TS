package org.apache.commons.math3.stat.inference;
import org.junit.Test;
import org.junit.Assert;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_basic() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1,2,3,4,5};
        double[] y = {6,7,8,9,10};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(0, result, 0.00001);
    }

    @Test
    public void testMannWhitneyU_emptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        try {
            double result = test.mannWhitneyU(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            //Expected exception to be thrown
        }
    }

    @Test
    public void testMannWhitneyU_nullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        try {
            double result = test.mannWhitneyU(x, y);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            //Expected exception to be thrown
        }
    }

    @Test
    public void testMannWhitneyUTest_basic() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1,2,3,4,5};
        double[] y = {6,7,8,9,10};
        double result = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUTest_emptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        try {
            double result = test.mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            //Expected exception to be thrown
        }
    }

    @Test
    public void testMannWhitneyUTest_nullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        try {
            double result = test.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            //Expected exception to be thrown
        }
    }

}