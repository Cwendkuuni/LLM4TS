import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testMannWhitneyUTestWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] result = test.concatenateSamples(x, y);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(10.0, 5, 5);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTestWithPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.0001);
    }

}