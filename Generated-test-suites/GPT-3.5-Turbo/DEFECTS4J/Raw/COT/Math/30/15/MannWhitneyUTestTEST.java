
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest uTest;

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        uTest = new MannWhitneyUTest();
        double result = uTest.mannWhitneyU(x, y);
        assertEquals(25.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        uTest = new MannWhitneyUTest();
        double result = uTest.mannWhitneyUTest(x, y);
        assertEquals(0.0290114185752220, result, 0.0001);
    }
}
