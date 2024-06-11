import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest mannWhitneyUTest;
    private double[] x;
    private double[] y;
    
    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
        x = new double[]{1, 2, 3, 4, 5};
        y = new double[]{6, 7, 8, 9, 10};
    }
    
    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyU_NullArgumentException() throws NullArgumentException, NoDataException {
        mannWhitneyUTest.mannWhitneyU(null, y);
    }
    
    @Test(expected=NoDataException.class)
    public void testMannWhitneyU_NoDataException() throws NullArgumentException, NoDataException {
        mannWhitneyUTest.mannWhitneyU(new double[]{}, y);
    }
    
    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double U = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(12.0, U, 0.0001);
    }
    
    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyUTest_NullArgumentException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(null, y);
    }
    
    @Test(expected=NoDataException.class)
    public void testMannWhitneyUTest_NoDataException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(new double[]{}, y);
    }
    
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.08333333333333337, pValue, 0.0001);
    }

}