import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0276, result, 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyU(x, y);
        assertEquals(0.0, result, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNull() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        double result = test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmpty() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};
        double result = test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTestTies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.MAXIMUM);
        double[] x = {1, 1, 2, 2, 3, 3, 4, 4};
        double[] y = {5, 5, 6, 6, 7, 7, 8, 8};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTestConvergence() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0276, result, 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceeded() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyUTest(x, y);
    }

}