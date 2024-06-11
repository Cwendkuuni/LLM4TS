import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_typicalCase() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        // Act
        double result = test.mannWhitneyU(x, y);

        // Assert
        assertEquals(15.0, result, 0);
    }

    @Test
    public void testMannWhitneyU_edgeCase() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1};
        double[] y = {2};

        // Act
        double result = test.mannWhitneyU(x, y);

        // Assert
        assertEquals(0.0, result, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_nullArgumentException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3};

        // Act
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_noDataException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3};

        // Act
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest_typicalCase() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        // Act
        double result = test.mannWhitneyUTest(x, y);

        // Assert
        assertTrue(result > 0.05);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_nullArgumentException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3};

        // Act
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_noDataException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3};

        // Act
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTest_maxCountExceededException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        // Act
        for (int i = 0; i < 1000; i++) {
            test.mannWhitneyUTest(x, y);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTest_convergenceException() {
        // Arrange
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 1, 1, 1};
        double[] y = {2, 2, 2, 2};

        // Act
        test.mannWhitneyUTest(x, y);
    }

}