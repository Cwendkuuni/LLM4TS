import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest testObject;

    @Before
    public void setUp() {
        testObject = new MannWhitneyUTest();
    }

    @Test
    public void testConstructor1() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    @Test
    public void testConstructor2() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.REMOVED, TiesStrategy.MAXIMUM);
        assertNotNull(mannWhitneyUTest);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNull() {
        testObject.ensureDataConformance(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmpty() {
        testObject.ensureDataConformance(new double[]{}, new double[]{});
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double[] result = testObject.concatenateSamples(x, y);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double result = testObject.mannWhitneyU(x, y);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double result = testObject.calculateAsymptoticPValue(5.0, 3, 2);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double result = testObject.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.001);
    }
}