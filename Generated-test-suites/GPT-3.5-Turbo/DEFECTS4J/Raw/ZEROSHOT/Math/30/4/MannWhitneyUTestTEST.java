import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthX() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthY() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(9.0, 3, 3);
        assertEquals(0.10560409560971, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.10560409560971, result, 0.001);
    }

}