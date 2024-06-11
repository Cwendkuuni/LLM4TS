import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testEnsureDataConformance_NullX_NullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Pass
        } catch (NoDataException e) {
            fail("Unexpected NoDataException");
        }
    }

    @Test
    public void testEnsureDataConformance_EmptyX_EmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException");
        } catch (NoDataException e) {
            // Pass
        }
    }

    @Test
    public void testMannWhitneyU_Simple() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyU_NullX_NullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Pass
        } catch (NoDataException e) {
            fail("Unexpected NoDataException");
        }
    }

    @Test
    public void testMannWhitneyUTest_Simple() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(1.0, result, 0.0001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCalculateAsymptoticPValue_Simple() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double result = test.calculateAsymptoticPValue(9.0, 3, 3);
            assertEquals(1.0, result, 0.0001);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }
}