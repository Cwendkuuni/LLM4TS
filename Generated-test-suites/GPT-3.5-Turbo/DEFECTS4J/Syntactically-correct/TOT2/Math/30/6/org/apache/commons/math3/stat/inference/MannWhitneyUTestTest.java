import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testEnsureDataConformance_NullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class,
                () -> test.ensureDataConformance(null, new double[]{1.0, 2.0}));
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyU_NullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class,
                () -> test.mannWhitneyU(null, new double[]{1.0, 2.0}));
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double pValue = test.calculateAsymptoticPValue(10, 5, 5);
        assertEquals(1.0, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTest_NullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class,
                () -> test.mannWhitneyUTest(null, new double[]{1.0, 2.0}));
    }

    @Test
    public void testMannWhitneyUTest_EmptySamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NoDataException.class,
                () -> test.mannWhitneyUTest(new double[]{}, new double[]{}));
    }

    @Test
    public void testMannWhitneyUTest_ConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 6.0, 7.0, 8.0};
        assertThrows(ConvergenceException.class,
                () -> test.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTest_MaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertThrows(MaxCountExceededException.class,
                () -> test.mannWhitneyUTest(x, y));
    }
}