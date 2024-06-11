import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test = new MannWhitneyUTest();

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4};
        double[] y = {5, 6, 7, 8};
        double result = test.mannWhitneyU(x, y);
        assertEquals(8.0, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArgument() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4};
        double[] y = null;
        double result = test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyData() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {5, 6, 7, 8};
        double result = test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4};
        double[] y = {5, 6, 7, 8};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0228, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArgument() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException  {
        double[] x = {1, 2, 3, 4};
        double[] y = null;
        double result = test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyData() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException  {
        double[] x = {};
        double[] y = {5, 6, 7, 8};
        double result = test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double result = test.calculateAsymptoticPValue(8, 4, 4);
        assertEquals(0.0228, result, 0.001);
    }

}