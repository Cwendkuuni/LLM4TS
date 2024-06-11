package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import java.util.Random;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test for default constructor.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest.naturalRanking);
    }

    /**
     * Test for constructor with specified NaN and ties strategies.
     */
    @org.junit.Test
    public void testConstructorWithSpecifiedStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.REPLACE, TiesStrategy.MIN);
        assertNotNull(mannWhitneyUTest.naturalRanking);
    }

    /**
     * Test {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @org.junit.Test
    public void testMannWhitneyU() {
        double[] arr1 = new double[]{1.0, 2.0, 3.0};
        double[] arr2 = new double[]{3.0, 2.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double result = mannWhitneyUTest.mannWhitneyU(arr1, arr2);
        assertEquals(1.5, result, 0.001);
    }

    /**
     * Test {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @org.junit.Test
    public void testMannWhitneyUTest() throws ConvergenceException,
            MaxCountExceededException, NoDataException,
            NullArgumentException {
        double[] arr1 = new double[]{1.0, 2.0, 3.0};
        double[] arr2 = new double[]{3.0, 2.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double pValue = mannWhitneyUTest.mannWhitneyUTest(arr1, arr2);
        assertEquals(0.16666666666666663, pValue, 0.001);
    }

    /**
     * Test {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @org.junit.Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyArrays() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullArrays() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullFirstArray() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
    }

    /**
     * Test {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullSecondArray() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, null);
    }

    /**
     * Test {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @org.junit.Test
    public void testConcatenateSamples() {
        double[] arr1 = new double[]{1.0, 2.0, 3.0};
        double[] arr2 = new double[]{3.0, 2.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] result = mannWhitneyUTest.concatenateSamples(arr1, arr2);

        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 3.0, 2.0, 1.0}, result);
    }

    /**
     * Test {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @org.junit.Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double minU = 4.0;
        int n1 = 5;
        int n2 = 7;

        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(minU, n1, n2);
        assertEquals(0.02269230769230769, pValue, 0.001);
    }
}