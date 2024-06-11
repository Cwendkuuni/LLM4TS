import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testEnsureDataConformance() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullX() {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyX() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] actual = mannWhitneyUTest.concatenateSamples(x, y);
        assertArrayEquals(expected, actual, 0.001);
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double actual = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(0.0, actual, 0.001);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyX() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        try {
            double actual = mannWhitneyUTest.calculateAsymptoticPValue(0.0, 3, 3);
            assertEquals(1.0, actual, 0.001);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double actual = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, actual, 0.001);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyX() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

}