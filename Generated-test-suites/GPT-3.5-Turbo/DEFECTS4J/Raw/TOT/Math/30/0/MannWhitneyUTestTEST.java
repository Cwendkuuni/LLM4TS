import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setup() {
        test = new MannWhitneyUTest();
    }

    // Test cases for constructor

    @Test
    public void testConstructor_withoutArguments_success() {
        assertNotNull(test);
    }

    @Test
    public void testConstructor_withArguments_success() {
        test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    // Test cases for ensureDataConformance

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformance_nullXArguments_failure() throws NullArgumentException, NoDataException {
        double[] y = {1, 2, 3};
        test.ensureDataConformance(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformance_nullYArguments_failure() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        test.ensureDataConformance(x, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformance_nullXYArguments_failure() throws NullArgumentException, NoDataException {
        test.ensureDataConformance(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformance_emptyXArguments_failure() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1, 2, 3};
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformance_emptyYArguments_failure() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.ensureDataConformance(x, y);
    }

    // Test cases for concatenateSamples

    @Test
    public void testConcatenateSamples_validArguments_success() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double[] expected = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.001);
    }

    // Test cases for mannWhitneyU

    @Test
    public void testMannWhitneyU_validArguments_success() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedU = 6.0;
        assertEquals(expectedU, test.mannWhitneyU(x, y), 0.001);
    }

    // Test cases for calculateAsymptoticPValue

    @Test
    public void testCalculateAsymptoticPValue_validArguments_success() throws ConvergenceException, MaxCountExceededException {
        double Umin = 6.0;
        int n1 = 3;
        int n2 = 3;
        double expectedPValue = 0.516;
        assertEquals(expectedPValue, test.calculateAsymptoticPValue(Umin, n1, n2), 0.001);
    }

    // Test cases for mannWhitneyUTest

    @Test
    public void testMannWhitneyUTest_validArguments_success() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedPValue = 0.516;
        assertEquals(expectedPValue, test.mannWhitneyUTest(x, y), 0.001);
    }

}