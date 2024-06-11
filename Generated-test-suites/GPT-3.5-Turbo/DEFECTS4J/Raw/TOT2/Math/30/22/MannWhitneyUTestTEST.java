import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyU(x, y);
        assertEquals(12.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.047619047619047616, pValue, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double pValue = test.calculateAsymptoticPValue(12, 5, 5);
        assertEquals(0.047619047619047616, pValue, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.mannWhitneyUTest(x, y);
    }
}