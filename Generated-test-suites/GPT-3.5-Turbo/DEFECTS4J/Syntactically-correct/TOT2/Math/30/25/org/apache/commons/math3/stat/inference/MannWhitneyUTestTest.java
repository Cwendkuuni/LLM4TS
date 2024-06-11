import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double[] z = test.concatenateSamples(x, y);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, z, 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double u = test.mannWhitneyU(x, y);
            assertEquals(6.0, u, 0.0001);
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
            double pValue = test.mannWhitneyUTest(x, y);
            assertTrue(pValue >= 0.0 && pValue <= 1.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}