import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestWithTypicalCase() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = uTest.mannWhitneyUTest(x, y);
            assertTrue(result >= 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testMannWhitneyUTestWithEdgeCase() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1.0};
        double[] y = {4.0};
        try {
            double result = uTest.mannWhitneyUTest(x, y);
            assertTrue(result >= 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testMannWhitneyUTestWithNullArgument() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4.0};
        try {
            uTest.mannWhitneyUTest(x, y);
            fail("NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            // Expected behavior
        } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArrays() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        try {
            uTest.mannWhitneyUTest(x, y);
            fail("NoDataException not thrown");
        } catch (NoDataException e) {
            // Expected behavior
        } catch (NullArgumentException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception");
        }
    }

}