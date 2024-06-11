import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    @Test
    public void testMannWhitneyUTestCustomConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.MAXIMAL, TiesStrategy.MINIMUM);
        assertNotNull(mannWhitneyUTest);
    }

    @Test
    public void testEnsureDataConformanceNullInput() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.ensureDataConformance(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertTrue(true);
        } catch (NoDataException e) {
            fail("Unexpected NoDataException");
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double[] z = mannWhitneyUTest.concatenateSamples(x, y);
        assertEquals(z.length, x.length + y.length);
        assertEquals(z[0], x[0], 0.0);
        assertEquals(z[x.length], y[0], 0.0);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double u = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, u, 0.0);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(6.0, 3, 2);
        assertEquals(1.0, pValue, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 0.0);
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

}