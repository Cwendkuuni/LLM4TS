
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(9.0, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullArgumentException_1() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullArgumentException_2() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_NoDataException_1() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_NoDataException_2() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyU_PotentialEdgeCase_1() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {3, 4, 2};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(7.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyU_PotentialEdgeCase_2() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 2};
        double[] y = {3, 3, 4};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(3.0, result, 0.0001);
    }
    
    @Test
    public void testMannWhitneyUTest_PotentialEdgeCase_1() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {3, 4, 2};
        double result = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.9286, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_PotentialEdgeCase_2() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 2};
        double[] y = {3, 3, 4};
        double result = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.1543, result, 0.0001);
    }
}

