import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setup() {
        test = new MannWhitneyUTest();
    }

    // Tests for the constructor

    @Test
    public void testConstructorDefault() {
        assertEquals(NaNStrategy.FIXED, test.getNaturalRanking().getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, test.getNaturalRanking().getTiesStrategy());
    }

    @Test
    public void testConstructorCustom() {
        MannWhitneyUTest customTest = new MannWhitneyUTest(NaNStrategy.MAXIMAL, TiesStrategy.MINIMUM);
        assertEquals(NaNStrategy.MAXIMAL, customTest.getNaturalRanking().getNanStrategy());
        assertEquals(TiesStrategy.MINIMUM, customTest.getNaturalRanking().getTiesStrategy());
    }

    // Tests for ensureDataConformance method

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullXY() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = null;
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.ensureDataConformance(x, y);
    }

    // Tests for concatenateSamples method

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] result = test.concatenateSamples(x, y);
        assertArrayEquals(expected, result, 0.0001);
    }

    // Test for mannWhitneyU method

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 12.0;
        double result = test.mannWhitneyU(x, y);
        assertEquals(expected, result, 0.0001);
    }

    // Tests for calculateAsymptoticPValue method

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 10.0;
        int n1 = 3;
        int n2 = 3;
        double expected = 0.5826;
        double result = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expected, result, 0.0001);
    }

    // Tests for mannWhitneyUTest method

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 0.5826;
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0001);
    }

}