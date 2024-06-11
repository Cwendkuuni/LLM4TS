import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double expectedU = 3.0;
        double actualU = mannWhitneyUTest.mannWhitneyU(x, y);

        assertEquals(expectedU, actualU, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_ZeroLengthX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_ZeroLengthY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 3;

        double expectedPValue = 1.0;
        double actualPValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);

        assertEquals(expectedPValue, actualPValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double expectedPValue = 1.0;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        assertEquals(expectedPValue, actualPValue, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_ZeroLengthX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_ZeroLengthY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }


}