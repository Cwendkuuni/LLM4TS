package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit test cases for MannWhitneyUTest class
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest()
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(MannWhitneyUTest.NaturalRanking.FIXED,
                mannWhitneyUTest.getNanStrategy());
        assertEquals(MannWhitneyUTest.NaturalRanking.AVERAGE,
                mannWhitneyUTest.getTiesStrategy());
    }

    /**
     * Test constructor MannWhitneyUTest(nanStrategy, tiesStrategy)
     */
    @Test
    public void testCustomConstructor() {
        NaNStrategy nanStrategy = MannWhitneyUTest.NaNStrategy.LOWER_MEAN;
        TiesStrategy tiesStrategy = MannWhitneyUTest.TiesStrategy.MIN;
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy,
                tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNanStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getTiesStrategy());
    }

    /**
     * Test method ensureDataConformance
     */
    @Test
    public void testEnsureDataConformance() {
        double[] x = null;
        double[] y = {1, 2, 3};
        try {
            MannWhitneyUTest. MannWhitneyUTest.ensureDataConformance(x, y);
            assert false;
        } catch (NullArgumentException e) {
            // expected
        }

        x = new double[0];
        y = new double[0];
        try {
            MannWhitneyUTest. MannWhitneyUTest.ensureDataConformance(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1};
        y = new double[] {1};
        MannWhitneyUTest. MannWhitneyUTest.ensureDataConformance(x, y);
    }

    /**
     * Test method mannWhitneyU
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1, 2, 3};
        double[] y = {3, 2, 1};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 1e-14);

        x = new double[] {1, 2, 2};
        y = new double[] {2, 1, 1};
        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(4.5, mannWhitneyU, 1e-14);

        x = new double[] {1, 1, 1, 1};
        y = new double[] {1, 1, 1};
        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 1e-14);

        x = new double[] {1, 1, 1, 1, 1};
        y = new double[] {1, 1, 1, 1};
        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(5.5, mannWhitneyU, 1e-14);

        x = new double[] {};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {};
        y = new double[] {1};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1, 1};
        y = new double[] {1, 1};
        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(2.5, mannWhitneyU, 1e-14);
    }

    /**
     * Test method mannWhitneyUTest
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1, 2, 3};
        double[] y = {3, 2, 1};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.01666666666666667, pValue, 1e-14);

        x = new double[] {1, 2, 2};
        y = new double[] {2, 1, 1};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.03333333333333333, pValue, 1e-14);

        x = new double[] {1, 1, 1, 1, 1};
        y = new double[] {1, 1, 1, 1};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.05, pValue, 1e-14);

        x = new double[] {1, 1, 1, 1, 1, 1};
        y = new double[] {1, 1, 1, 1, 1, 1};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.05555555555555555, pValue, 1e-14);

        x = new double[] {};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {};
        y = new double[] {1};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1};
        y = new double[] {};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            assert false;
        } catch (NoDataException e) {
            // expected
        }

        x = new double[] {1, 1};
        y = new double[] {1, 1};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.5, pValue, 1e-14);
    }

    /**
     * Test method calculateAsymptoticPValue
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double Umin = 4.0;
        int n1 = 3;
        int n2 = 3;
        try {
            double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
            assert false;
        } catch (MaxCountExceededException e) {
            // expected
        }

        Umin = 12.0;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.000278, pValue, 1e-14);

        Umin = 10.5;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.001359, pValue, 1e-14);

        Umin = 6.5;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.01666666666666667, pValue, 1e-14);
    }
}