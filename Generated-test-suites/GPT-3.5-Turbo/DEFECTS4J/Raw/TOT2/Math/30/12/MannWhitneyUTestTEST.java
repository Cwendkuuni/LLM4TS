import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    // Test cases for MannWhitneyUTest class

    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEmptySamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyU(x, y);
        assertEquals(25.0, result, 0.0001);
    }

    @Test
    public void testAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(15.0, 5, 5);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.0001);
    }

}