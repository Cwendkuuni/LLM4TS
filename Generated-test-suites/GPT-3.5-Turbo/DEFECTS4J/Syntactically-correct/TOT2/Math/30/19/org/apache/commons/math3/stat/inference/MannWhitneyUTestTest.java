import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testMannWhitneyUTestWithStrategiesConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[]{}, new double[]{});
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] result = test.concatenateSamples(x, y);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        assertArrayEquals(expected, result, 0.0);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.0);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(9.0, 3, 3);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.0);
    }

}