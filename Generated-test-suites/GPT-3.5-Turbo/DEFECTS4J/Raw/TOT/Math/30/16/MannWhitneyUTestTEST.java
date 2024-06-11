import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    // Test cases for constructor

    @Test
    public void testConstructorDefault() {
        assertNotNull(test);
    }

    @Test
    public void testConstructorWithStrategies() {
        test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    // Test cases for ensureDataConformance

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullX() {
        test.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullY() {
        test.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyX() {
        test.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyY() {
        test.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    // Test cases for concatenateSamples

    @Test
    public void testConcatenateSamples() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.0);
    }

    // Test cases for mannWhitneyU

    @Test
    public void testMannWhitneyU() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};
        double expected = 5.0;
        try {
            assertEquals(expected, test.mannWhitneyU(x, y), 0.0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullX() {
        test.mannWhitneyU(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullY() {
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyX() {
        test.mannWhitneyU(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyY() {
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    // Test cases for calculateAsymptoticPValue

    @Test
    public void testCalculateAsymptoticPValue() {
        double Umin = 5.0;
        int n1 = 3;
        int n2 = 2;
        double expected = 0.25;
        try {
            assertEquals(expected, test.calculateAsymptoticPValue(Umin, n1, n2), 0.0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueWithConvergenceException() {
        double Umin = 5.0;
        int n1 = 0;
        int n2 = 2;
        test.calculateAsymptoticPValue(Umin, n1, n2);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testCalculateAsymptoticPValueWithMaxCountExceededException() {
        double Umin = 5.0;
        int n1 = 2;
        int n2 = 0;
        test.calculateAsymptoticPValue(Umin, n1, n2);
    }

    // Test cases for mannWhitneyUTest

    @Test
    public void testMannWhitneyUTest() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};
        double expected = 0.35;
        try {
            assertEquals(expected, test.mannWhitneyUTest(x, y), 0.0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullX() {
        test.mannWhitneyUTest(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullY() {
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyX() {
        test.mannWhitneyUTest(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyY() {
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceException() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};
        test = new MannWhitneyUTest(NaNStrategy.MAXIMAL, TiesStrategy.MINIMUM);
        try {
            test.mannWhitneyUTest(x, y);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};
        test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.MAXIMUM);
        try {
            test.mannWhitneyUTest(x, y);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}