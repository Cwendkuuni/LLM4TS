import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testEnsureDataConformance_NullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(null, null);
            fail("NullArgumentException should have been thrown.");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEnsureDataConformance_EmptyArray() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(new double[]{}, new double[]{});
            fail("NoDataException should have been thrown.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0};

        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double expected = 4.0;

        assertEquals(expected, test.mannWhitneyU(x, y), 0.0001);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 2.0;
        int n1 = 3;
        int n2 = 2;
        double expected = 1.0;

        assertEquals(expected, test.calculateAsymptoticPValue(Umin, n1, n2), 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double expected = 1.0;

        try {
            assertEquals(expected, test.mannWhitneyUTest(x, y), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}