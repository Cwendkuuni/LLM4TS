import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(9, result, 0);
        } catch (NullArgumentException | NoDataException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(1, result, 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}