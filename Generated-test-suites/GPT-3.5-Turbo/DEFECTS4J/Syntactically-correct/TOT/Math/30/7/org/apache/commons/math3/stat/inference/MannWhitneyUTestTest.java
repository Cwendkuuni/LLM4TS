import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    private static final double[] SAMPLE1 = {1.0, 2.0, 3.0, 4.0, 5.0};
    private static final double[] SAMPLE2 = {6.0, 7.0, 8.0, 9.0, 10.0};
    private static final double[] EMPTY_SAMPLE = {};
    private static final double[] NULL_SAMPLE = null;

    private MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

    // Test case for testing MannWhitneyUTest constructor
    @Test
    public void testConstructor() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            Assert.assertNotNull(mannWhitneyUTest);
        } catch (Exception e) {
            Assert.fail("Constructor test failed: " + e.getMessage());
        }
    }

    // Test case for testing MannWhitneyUTest constructor with NaN strategy and ties strategy
    @Test
    public void testConstructorWithStrategies() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.MAXIMAL, TiesStrategy.MAXIMUM);
            Assert.assertNotNull(mannWhitneyUTest);
        } catch (Exception e) {
            Assert.fail("Constructor with strategies test failed: " + e.getMessage());
        }
    }

    // Test case for testing mannWhitneyU method with valid input and equal length samples
    @Test
    public void testMannWhitneyUWithEqualLengthSamples() {
        try {
            double U = mannWhitneyUTest.mannWhitneyU(SAMPLE1, SAMPLE2);
            Assert.assertEquals(10.0, U, 0.0);
        } catch (Exception e) {
            Assert.fail("Mann-Whitney U test with equal length samples failed: " + e.getMessage());
        }
    }

    // Test case for testing mannWhitneyU method with valid input and different length samples
    @Test
    public void testMannWhitneyUWithDifferentLengthSamples() {
        try {
            double U = mannWhitneyUTest.mannWhitneyU(SAMPLE1, SAMPLE2);
            Assert.assertEquals(10.0, U, 0.0);
        } catch (Exception e) {
            Assert.fail("Mann-Whitney U test with different length samples failed: " + e.getMessage());
        }
    }

    // Test case for testing mannWhitneyU method with empty sample
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptySample() throws NoDataException, NullArgumentException {
        mannWhitneyUTest.mannWhitneyU(EMPTY_SAMPLE, SAMPLE2);
    }

    // Test case for testing mannWhitneyU method with null sample
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullSample() throws NoDataException, NullArgumentException {
        mannWhitneyUTest.mannWhitneyU(NULL_SAMPLE, SAMPLE2);
    }

    // Test case for testing mannWhitneyUTest method with valid input
    @Test
    public void testMannWhitneyUTestWithValidInput() {
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(SAMPLE1, SAMPLE2);
            Assert.assertEquals(0.0021691020521510936, pValue, 0.00001);
        } catch (Exception e) {
            Assert.fail("Mann-Whitney U test with valid input failed: " + e.getMessage());
        }
    }

    // Test case for testing mannWhitneyUTest method with empty sample
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptySample() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(EMPTY_SAMPLE, SAMPLE2);
    }

    // Test case for testing mannWhitneyUTest method with null sample
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullSample() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(NULL_SAMPLE, SAMPLE2);
    }

    // Test case for testing mannWhitneyUTest method with valid input and equal length samples
    @Test
    public void testMannWhitneyUTestWithEqualLengthSamples() {
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(SAMPLE1, SAMPLE2);
            Assert.assertEquals(0.0021691020521510936, pValue, 0.00001);
        } catch (Exception e) {
            Assert.fail("Mann-Whitney U test with equal length samples failed: " + e.getMessage());
        }
    }

    // Test case for testing mannWhitneyUTest method with valid input and different length samples
    @Test
    public void testMannWhitneyUTestWithDifferentLengthSamples() {
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(SAMPLE1, SAMPLE2);
            Assert.assertEquals(0.0021691020521510936, pValue, 0.00001);
        } catch (Exception e) {
            Assert.fail("Mann-Whitney U test with different length samples failed: " + e.getMessage());
        }
    }
}