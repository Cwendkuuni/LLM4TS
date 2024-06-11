package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /** Ranking algorithm. */
    private NaturalRanking naturalRanking;

    /**
     * Setup method to initialize test variables.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setup() {
        naturalRanking = new NaturalRanking(NaturalRanking.DEFAULT_STRATEGY,
                NaturalRanking.DEFAULT_TIES_STRATEGY);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * <p>
     * This test method checks the validity of the Mann-Whitney U statistic
     * calculation when given valid inputs.
     * </p>
     * <p>
     * This test method creates two arrays with known ranks and checks if the
     * Mann-Whitney U statistic calculation matches the expected result.
     * </p>
     *
     * @throws NullArgumentException if inputs are null.
     * @throws NoDataException if inputs are empty.
     */
    @Test
    public void testMannWhitneyUValid() throws NullArgumentException,
            NoDataException {

        // Given: Two samples with known ranks
        final double[] x = {1.5, 2.3, 3.1, 5.2};
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U statistic is calculated
         */
        final double U = new MannWhitneyUTest().mannWhitneyU(x, y);

        /*
         * Then: Verify the Mann-Whitney U statistic matches the expected result
         */
        Assert.assertEquals(10.0, U, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * <p>
     * This test method checks the validity of the Mann-Whitney U test calculation
     * when given valid inputs and returns the correct p-value.
     * </p>
     * <p>
     * This test method creates two arrays with known ranks and checks if the
     * Mann-Whitney U test calculation returns the expected p-value.
     * </p>
     *
     * @throws NullArgumentException if inputs are null.
     * @throws NoDataException if inputs are empty.
     * @throws ConvergenceException if calculation fails due to a convergence error.
     * @throws MaxCountExceededException if calculation fails due to exceeding
     * the maximum number of iterations.
     */
    @Test
    public void testMannWhitneyUTestValid() throws NullArgumentException,
            NoDataException, ConvergenceException, MaxCountExceededException {

        // Given: Two samples with known ranks
        final double[] x = {1.5, 2.3, 3.1, 5.2};
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U test is performed
         */
        final double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);

        /*
         * Then: Verify the p-value matches the expected result
         */
        Assert.assertEquals(0.05, pValue, 0.01);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * <p>
     * This test method checks the behavior of the Mann-Whitney U statistic
     * calculation when given invalid inputs (null and empty arrays).
     * </p>
     * <p>
     * This test method checks for {@link NullArgumentException} and
     * {@link NoDataException} exceptions.
     * </p>
     *
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUInvalid_Null() {

        // Given: Null input for sample x
        final double[] x = null;
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U statistic is calculated
         */
        new MannWhitneyUTest().mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUInvalid_Empty() {

        // Given: Empty input for sample x
        final double[] x = {};
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U statistic is calculated
         */
        new MannWhitneyUTest().mannWhitneyU(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * <p>
     * This test method checks the behavior of the Mann-Whitney U test
     * calculation when given invalid inputs (null and empty arrays).
     * </p>
     * <p>
     * This test method checks for {@link NullArgumentException} and
     * {@link NoDataException} exceptions.
     * </p>
     *
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestInvalid_Null() {

        // Given: Null input for sample x
        final double[] x = null;
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U test is performed
         */
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestInvalid_Empty() {

        // Given: Empty input for sample x
        final double[] x = {};
        final double[] y = {2.9, 2.1, 3.7, 6.0};

        /*
         * When: Mann-Whitney U test is performed
         */
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }
}