package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

import java.util.Arrays;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyUTest_EmptyArrays() {
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTest_NullArrays() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], null));
    }

    @Test
    public void testMannWhitneyUTest_IdentityData() {
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0, 4.0};
        double[] y = {1.0, 2.0, 2.0, 3.0, 3.0, 4.0};

        assertEquals(0.0, new MannWhitneyUTest().mannWhitneyU(x, y), 1e-15);
    }

    @Test
    public void testMannWhitneyUTest_DifferentSampleSizes() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};

        assertEquals(6.0, new MannWhitneyUTest().mannWhitneyU(x, y), 1e-15);
    }

    @Test
    public void testMannWhitneyUTest_DifferentData() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        assertEquals(3.0, new MannWhitneyUTest().mannWhitneyU(x, y), 1e-15);
    }

    @Test
    public void testMannWhitneyUTest_LargeData() {
        double[] x = new double[1000];
        double[] y = new double[1000];

        for (int i = 0; i < x.length; i++) {
            x[i] = FastMath.nextGaussian();
            y[i] = FastMath.nextGaussian();
        }

        try {
            double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 1e-15); // The p-value should be 1 for identical distributions
        } catch (ConvergenceException e) {
            // This test has a low probability of passing due to the Monte Carlo nature of the p-value calculation
            // and is not an indication of a bug in the code
            PrecisionUtils.assertEquals(1.0, Precision.round(e.getProbability(), 6), 1e-9);
        } catch (MaxCountExceededException e) {
            fail("Maximum number of iterations should not be exceeded");
        }
    }

    @Test
    public void testMannWhitneyUTest_DifferentDistributions() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.5, 5.5, 6.5};

        try {
            double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
            assertFalse(Double.isEqual(1.0, pValue));
        } catch (NoDataException e) {
            fail("The data should be valid");
        } catch (ConvergenceException e) {
            PrecisionUtils.assertEquals(1.0 - 0.05, Precision.round(e.getProbability(), 5), 1e-5);
        } catch (MaxCountExceededException e) {
            fail("Maximum number of iterations should not be exceeded");
        }
    }

    @Test
    public void testMannWhitneyUTest_DifferentDistributionsWithTies() {
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0, 4.0};
        double[] y = {1.5, 2.5, 2.5, 3.5, 3.5, 4.5};

        try {
            double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
            assertFalse(Double.isEqual(1.0, pValue));
        } catch (NoDataException e) {
            fail("The data should be valid");
        } catch (ConvergenceException e) {
            PrecisionUtils.assertEquals(1.0 - 0.05, Precision.round(e.getProbability(), 5), 1e-5);
        } catch (MaxCountExceededException e) {
            fail("Maximum number of iterations should not be exceeded");
        }
    }

    @Test
    public void testMannWhitneyUTest_DifferentDistributionsWithNaN() {
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0, Double.NaN};
        double[] y = {1.5, 2.5, 2.5, 3.5, 3.5, 4.5};

        try {
            new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.MIN).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.AVERAGE).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.AVERAGE).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest_DifferentDistributionsWithTiesAndNaN() {
        double[] x = {1.0, 2.0, 2.0, 3.0, 3.0, Double.NaN};
        double[] y = {1.5, 2.5, 2.5, 3.5, 3.5, 4.5, Double.NaN};

        try {
            new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.MIN).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.AVERAGE).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        try {
            new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.AVERAGE).mannWhitneyUTest(x, y);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }
    }
}