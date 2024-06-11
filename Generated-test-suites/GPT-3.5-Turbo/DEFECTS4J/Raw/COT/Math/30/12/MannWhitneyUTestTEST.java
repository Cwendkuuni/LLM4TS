
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(15, result, 0);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        } catch (NoDataException e) {
            fail("NoDataException should not be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(1, result, 0);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        } catch (NoDataException e) {
            fail("NoDataException should not be thrown");
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }
    }
    
    // Add additional test cases for all public methods in MannWhitneyUTest class

}
