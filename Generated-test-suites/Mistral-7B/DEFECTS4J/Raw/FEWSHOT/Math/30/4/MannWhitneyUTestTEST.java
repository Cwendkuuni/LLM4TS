package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        // Test empty arrays
        MannWhitneyUTest mwuTest = new MannWhitneyUTest();
        try {
            mwuTest.mannWhitneyUTest(new double[0][], new double[0][]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        try {
            mwuTest.mannWhitneyUTest(new double[0][], new double[0][]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test null arrays
        try {
            mwuTest.mannWhitneyUTest(null, new double[0][]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            mwuTest.mannWhitneyUTest(new double[0][], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test Mann-Whitney U statistic calculation
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {3.0, 4.0, 5.0};
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double uStatistic = mwu.mannWhitneyU(x, y);
        assertEquals(6.0, uStatistic, 0.001);

        // Test p-value calculation
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 1.0);

        // Test p-value calculation with ties
        MannWhitneyUTest mwuWithTies = new MannWhitneyUTest(TiesStrategy.DMS,
                NaNStrategy.REMOVED);
        pValue = mwuWithTies.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 1.0);

        // Test convergence and maximum iterations exceptions
        try {
            MannWhitneyUTest mwuWithMaxIterations = new MannWhitneyUTest() {
                @Override
                int getMaxIterations() {
                    return 0;
                }
            };
            mwuWithMaxIterations.mannWhitneyUTest(x, y);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            MannWhitneyUTest mwuWithConvergence = new MannWhitneyUTest() {
                @Override
                double calculateAsymptoticPValue(double Umin, int n1, int n2) {
                    throw new ConvergenceException();
                }
            };
            mwuWithConvergence.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        NormalDistribution standardNormal = new NormalDistribution(0, 1);

        // Test when Umin is greater than EU
        double Umin = 2.0;
        int n1 = 5;
        int n2 = 10;
        double expectedPValue = 2 * standardNormal.cumulativeProbability(
                (Umin - (n1 * n2) / 2.0) / Math.sqrt((n1 * n2 * (n1 + n2 + 1)) / 12.0));
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double calculatedPValue = mwu.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expectedPValue, calculatedPValue, 0.001);

        // Test when Umin is less than EU
        Umin = 1.0;
        n1 = 5;
        n2 = 10;
        expectedPValue = 2 * standardNormal.cumulativeProbability(
                (Umin - (n1 * n2) / 2.0) / Math.sqrt((n1 * n2 * (n1 + n2 + 1)) / 12.0));
        calculatedPValue = mwu.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expectedPValue, calculatedPValue, 0.001);

        // Test when Umin is equal to EU
        Umin = (n1 * n2) / 2.0;
        expectedPValue = 1.0;
        calculatedPValue = mwu.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expectedPValue, calculatedPValue, 0.001);
    }
}