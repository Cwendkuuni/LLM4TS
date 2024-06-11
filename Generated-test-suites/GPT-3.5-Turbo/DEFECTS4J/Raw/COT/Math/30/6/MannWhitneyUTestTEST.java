
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};

        double result = test.mannWhitneyU(x, y);

        Assert.assertEquals(15.0, result, 0.001);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 10.0;
        int n1 = 5;
        int n2 = 5;

        double result = test.calculateAsymptoticPValue(Umin, n1, n2);

        Assert.assertEquals(0.056, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException,
            ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};

        double result = test.mannWhitneyUTest(x, y);

        Assert.assertEquals(0.112, result, 0.001);
    }
}
