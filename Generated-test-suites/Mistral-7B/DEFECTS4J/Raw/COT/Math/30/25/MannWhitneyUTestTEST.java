package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Test;

/**
 * JUnit test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method mannWhitneyU().
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 3.0, 2.0, 1.0 };
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method calculateAsymptoticPValue().
     */
    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 3;
        double asymptoticPValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0286, asymptoticPValue, 0.001);
    }

    /**
     * Test method mannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 3.0, 2.0, 1.0 };
        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0286, asymptoticPValue, 0.001);
    }

    /**
     * Test constructor MannWhitneyUTest with custom strategies.
     */
    @Test
    public void testConstructorWithCustomStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                org.apache.commons.math3.stat.ranking.MedianRanking.INSTANCE,
                org.apache.commons.math3.stat.ranking.DenseTiesStrategy.TIE_ONE);
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method mannWhitneyU() with custom strategies.
     */
    @Test
    public void testMannWhitneyUWithCustomStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                org.apache.commons.math3.stat.ranking.MedianRanking.INSTANCE,
                org.apache.commons.math3.stat.ranking.DenseTiesStrategy.TIE_ONE);
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 3.0, 2.0, 1.0 };
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method calculateAsymptoticPValue() with custom strategies.
     */
    @Test
    public void testCalculateAsymptoticPValueWithCustomStrategies() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                org.apache.commons.math3.stat.ranking.MedianRanking.INSTANCE,
                org.apache.commons.math3.stat.ranking.DenseTiesStrategy.TIE_ONE);
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 3;
        double asymptoticPValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0286, asymptoticPValue, 0.001);
    }

    /**
     * Test method mannWhitneyUTest() with custom strategies.
     */
    @Test
    public void testMannWhitneyUTestWithCustomStrategies() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                org.apache.commons.math3.stat.ranking.MedianRanking.INSTANCE,
                org.apache.commons.math3.stat.ranking.DenseTiesStrategy.TIE_ONE);
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 3.0, 2.0, 1.0 };
        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0286, asymptoticPValue, 0.001);
    }

    /**
     * Test method ensureDataConformance().
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method ensureDataConformance().
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method concatenateSamples().
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 3.0, 2.0, 1.0 };
        double[] concatenatedSamples = mannWhitneyUTest.concatenateSamples(x, y);
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 3.0, 2.0, 1.0 }, concatenatedSamples);
    }
}