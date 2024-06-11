import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testEnsureDataConformance_NullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(null, null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEnsureDataConformance_ZeroLength() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(new double[]{}, new double[]{});
            fail("NoDataException expected");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] z = test.concatenateSamples(x, y);

        assertEquals(4, z.length);
        assertEquals(1.0, z[0], 0.0001);
        assertEquals(2.0, z[1], 0.0001);
        assertEquals(3.0, z[2], 0.0001);
        assertEquals(4.0, z[3], 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double U = test.mannWhitneyU(x, y);
            assertEquals(9.0, U, 0.0001);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double pValue = test.mannWhitneyUTest(x, y);
            // Check if p-value is within acceptable range
            assertTrue(pValue >= 0 && pValue <= 1);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double pValue = test.calculateAsymptoticPValue(5.0, 3, 2);
            // Check if p-value is within acceptable range
            assertTrue(pValue >= 0 && pValue <= 1);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

}