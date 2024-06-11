import static org.junit.Assert.*;

import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testEnsureDataConformanceNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double[] result = test.concatenateSamples(x, y);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(0.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double result = test.calculateAsymptoticPValue(0.0, 3, 2);
            assertEquals(1.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(1.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}