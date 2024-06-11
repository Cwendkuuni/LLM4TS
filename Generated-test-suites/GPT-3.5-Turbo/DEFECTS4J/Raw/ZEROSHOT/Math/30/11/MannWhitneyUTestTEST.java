import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        double[] x = {1, 1, 1, 2, 3, 4, 5};
        double[] y = {1, 2, 2, 3, 4, 5};
        double expected = 34.0;
        double result = test.mannWhitneyU(x, y);
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUWithEmptySamples() throws Exception {
        double[] x = {};
        double[] y = {};
        try {
            test.mannWhitneyU(x, y);
            fail("NoDataException not thrown for empty samples");
        } catch (NoDataException e) {
        }
    }

    @Test
    public void testMannWhitneyUWithNullSamples() throws Exception {
        double[] x = null;
        double[] y = null;
        try {
            test.mannWhitneyU(x, y);
            fail("NullArgumentException not thrown for null samples");
        } catch (NullArgumentException e) {
        }
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = {1, 1, 1, 2, 3, 4, 5};
        double[] y = {1, 2, 2, 3, 4, 5};
        double expected = 0.133104;
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUTestWithEmptySamples() throws Exception {
        double[] x = {};
        double[] y = {};
        try {
            test.mannWhitneyUTest(x, y);
            fail("NoDataException not thrown for empty samples");
        } catch (NoDataException e) {
        }
    }

    @Test
    public void testMannWhitneyUTestWithNullSamples() throws Exception {
        double[] x = null;
        double[] y = null;
        try {
            test.mannWhitneyUTest(x, y);
            fail("NullArgumentException not thrown for null samples");
        } catch (NullArgumentException e) {
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        double Umin = 5.0;
        int n1 = 4;
        int n2 = 6;
        double expected = 0.016503;
        double result = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testCalculateAsymptoticPValueWithNegativeUmin() throws Exception {
        double Umin = -5.0;
        int n1 = 4;
        int n2 = 6;
        try {
            test.calculateAsymptoticPValue(Umin, n1, n2);
            fail("ConvergenceException not thrown for negative Umin");
        } catch (ConvergenceException e) {
        }
    }

    @Test
    public void testCalculateAsymptoticPValueWithZeroVarU() throws Exception {
        double Umin = 5.0;
        int n1 = 0;
        int n2 = 0;
        try {
            test.calculateAsymptoticPValue(Umin, n1, n2);
            fail("MaxCountExceededException not thrown for zero VarU");
        } catch (MaxCountExceededException e) {
        }
    }
}