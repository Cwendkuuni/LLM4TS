import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest testObject;

    @Before
    public void setUp() {
        testObject = new MannWhitneyUTest();
    }

    // Test case for MannWhitneyUTest constructor
    @Test
    public void testConstructor() {
        assertNotNull(testObject);
    }

    // Test case for mannWhitneyU method with valid inputs
    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};

        double result = testObject.mannWhitneyU(x, y);

        assertEquals(15, result, 0.00001);
    }

    // Test case for mannWhitneyU method with different input lengths
    @Test
    public void testMannWhitneyUWithDifferentLengths() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0};

        double result = testObject.mannWhitneyU(x, y);

        assertEquals(20, result, 0.00001);
    }

    // Test case for mannWhitneyU method with empty input arrays
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyArrays() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {};

        testObject.mannWhitneyU(x, y);
    }

    // Test case for mannWhitneyU method with null input arrays
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArrays() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = null;

        testObject.mannWhitneyU(x, y);
    }

    // Test case for calculateAsymptoticPValue method with valid inputs
    @Test
    public void testCalculateAsymptoticPValue() throws MaxCountExceededException, ConvergenceException {
        double result = testObject.calculateAsymptoticPValue(70, 10, 15);

        assertEquals(0.720171783, result, 0.00001);
    }

    // Test case for calculateAsymptoticPValue method with Umin equals to EU
    @Test
    public void testCalculateAsymptoticPValueWithUminEqualsToEU() throws MaxCountExceededException, ConvergenceException {
        double result = testObject.calculateAsymptoticPValue(75, 10, 15);

        assertEquals(1, result, 0.00001);
    }

    // Test case for calculateAsymptoticPValue method with Umin greater than EU
    @Test
    public void testCalculateAsymptoticPValueWithUminGreaterThanEU() throws MaxCountExceededException, ConvergenceException {
        double result = testObject.calculateAsymptoticPValue(80, 10, 15);

        assertEquals(0.717034775, result, 0.00001);
    }

    // Test case for mannWhitneyUTest method with valid inputs
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};

        double result = testObject.mannWhitneyUTest(x, y);

        assertEquals(0.316993295, result, 0.00001);
    }

    // Test case for mannWhitneyUTest method with different input lengths
    @Test
    public void testMannWhitneyUTestWithDifferentLengths() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0};

        double result = testObject.mannWhitneyUTest(x, y);

        assertEquals(0.920416699, result, 0.00001);
    }

    // Test case for mannWhitneyUTest method with empty input arrays
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArrays() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {};

        testObject.mannWhitneyUTest(x, y);
    }

    // Test case for mannWhitneyUTest method with null input arrays
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArrays() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = null;

        testObject.mannWhitneyUTest(x, y);
    }

}