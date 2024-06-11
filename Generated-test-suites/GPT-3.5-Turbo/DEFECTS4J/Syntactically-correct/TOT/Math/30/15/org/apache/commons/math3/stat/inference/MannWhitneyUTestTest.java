import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setup() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    // Test mannWhitneyU method

    @Test
    public void testMannWhitneyU_SampleSizes1_ShouldReturnCorrectUStatistic() throws NullArgumentException, NoDataException {
        // Arrange
        double[] x = {1};
        double[] y = {2};

        // Act
        double result = mannWhitneyUTest.mannWhitneyU(x, y);

        // Assert
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testMannWhitneyU_SampleSizes2_ShouldReturnCorrectUStatistic() throws NullArgumentException, NoDataException {
        // Arrange
        double[] x = {1, 2};
        double[] y = {3, 4};

        // Act
        double result = mannWhitneyUTest.mannWhitneyU(x, y);

        // Assert
        Assert.assertEquals(1, result, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSample_ShouldThrowNullArgumentException() throws NullArgumentException, NoDataException {
        // Arrange
        double[] x = null;
        double[] y = {1, 2};

        // Act
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptySample_ShouldThrowNoDataException() throws NullArgumentException, NoDataException {
        // Arrange
        double[] x = {};
        double[] y = {1, 2};

        // Act
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    // Test mannWhitneyUTest method

    @Test
    public void testMannWhitneyUTest_SampleSizes1_ShouldReturnCorrectPValue() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = {1};
        double[] y = {2};

        // Act
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);

        // Assert
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testMannWhitneyUTest_SampleSizes2_ShouldReturnCorrectPValue() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = {1, 2};
        double[] y = {3, 4};

        // Act
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);

        // Assert
        Assert.assertEquals(1, result, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSample_ShouldThrowNullArgumentException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = null;
        double[] y = {1, 2};

        // Act
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptySample_ShouldThrowNoDataException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = {};
        double[] y = {1, 2};

        // Act
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest_ConvergenceError_ShouldThrowConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = {1, 2, 3, 4};
        double[] y = {5, 6, 7, 8};

        // Act
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            Assert.fail("Exception not thrown");
        } catch (ConvergenceException e) {
            // Assert
            Assert.assertEquals("Convergence Error", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_MaxIterationsExceeded_ShouldThrowMaxCountExceededException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        // Arrange
        double[] x = {1, 2, 3, 4};
        double[] y = {5, 6, 7, 8};

        // Act
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            Assert.fail("Exception not thrown");
        } catch (MaxCountExceededException e) {
            // Assert
            Assert.assertEquals("Max Iterations Exceeded", e.getMessage());
        }
    }

}