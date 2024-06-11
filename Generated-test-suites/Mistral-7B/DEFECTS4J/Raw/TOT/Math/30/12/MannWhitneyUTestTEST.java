package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        // normal case
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
        Assert.assertEquals("Mann-Whitney U for normal case", 10.0, mannWhitneyU, 0.0);

        // empty samples
        Assert.assertThrows(NoDataException.class, () -> new MannWhitneyUTest().mannWhitneyU(new double[0], new double[0]));

        // null samples
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyU(null, new double[0]));
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyU(new double[0], null));

        // one sample is empty
        double[] xEmpty = new double[0];
        double mannWhitneyUEmptyX = new MannWhitneyUTest().mannWhitneyU(xEmpty, y);
        Assert.assertEquals("Mann-Whitney U for empty x case", 0.0, mannWhitneyUEmptyX, 0.0);

        // one sample is null
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyU(null, y));

        // samples are equal
        double[] xEqual = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] yEqual = {3.0, 3.0, 3.0, 3.0, 3.0};
        double mannWhitneyUEqual = new MannWhitneyUTest().mannWhitneyU(xEqual, yEqual);
        Assert.assertEquals("Mann-Whitney U for equal samples case", 6.0, mannWhitgitneyUEqual, 0.0);

        // samples have different length
        double[] xShort = {1.0, 2.0};
        double[] yLong = {1.0, 2.0, 3.0, 4.0, 5.0};
        double mannWhitneyUShortLong = new MannWhitneyUTest().mannWhitneyU(xShort, yLong);
        Assert.assertEquals("Mann-Whitney U for samples of different length case", 4.0, mannWhitneyUShortLong, 0.0);

        // NaN's in samples
        double[] xNaN = {1.0, Double.NaN, 3.0};
        double[] yNaN = {4.0, 5.0, Double.NaN};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.NaNStrategy.SKIP, MannWhitneyUTest.TiesStrategy.MIN);
        double mannWhitneyUNaNN = mannWhitneyUTest.mannWhitneyU(xNaN, yNaN);
        Assert.assertEquals("Mann-Whitney U for samples with NaN's case", 6.0, mannWhitneyUNaNN, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        // normal case
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        Assert.assertEquals("p-value for normal case", 0.05, pValue, 0.01);

        // empty samples
        Assert.assertThrows(NoDataException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]));

        // null samples
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(null, new double[0]));
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], null));

        // one sample is empty
        double[] xEmpty = new double[0];
        double pValueEmptyX = new MannWhitneyUTest().mannWhitneyUTest(xEmpty, y);
        Assert.assertEquals("p-value for empty x case", 1.0, pValueEmptyX, 0.0);

        // one sample is null
        Assert.assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(null, y));

        // samples are equal
        double[] xEqual = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] yEqual = {3.0, 3.0, 3.0, 3.0, 3.0};
        double pValueEqual = new MannWhitneyUTest().mannWhitneyUTest(xEqual, yEqual);
        Assert.assertEquals("p-value for equal samples case", 1.0, pValueEqual, 0.01);

        // samples have different length
        double[] xShort = {1.0, 2.0};
        double[] yLong = {1.0, 2.0, 3.0, 4.0, 5.0};
        double pValueShortLong = new MannWhitneyUTest().mannWhitneyUTest(xShort, yLong);
        Assert.assertEquals("p-value for samples of different length case", 0.05, pValueShortLong, 0.01);

        // NaN's in samples
        double[] xNaN = {1.0, Double.NaN, 3.0};
        double[] yNaN = {4.0, 5.0, Double.NaN};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.NaNStrategy.SKIP, MannWhitneyUTest.TiesStrategy.MIN);
        double pValueNaNN = mannWhitneyUTest.mannWhitneyUTest(xNaN, yNaN);
        Assert.assertEquals("p-value for samples with NaN's case", 0.1, pValueNaNN, 0.01);

        // ConvergenceException
        double[] xConvergence = {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
        double[] yConvergence = {1.0, 2.0, 3.0};
        Assert.assertThrows(ConvergenceException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(xConvergence, yConvergence));

        // MaxCountExceededException
        double[] xMaxCount = new double[Integer.MAX_VALUE];
        double[] yMaxCount = new double[Integer.MAX_VALUE];
        Assert.assertThrows(MaxCountExceededException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(xMaxCount, yMaxCount));
    }

}