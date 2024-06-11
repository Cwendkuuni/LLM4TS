
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testEnsureDataConformance() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(x, y);
        } catch (NullArgumentException | NoDataException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] concatenated = test.concatenateSamples(x, y);
        assertEquals(6.0, concatenated[5], 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double U = test.mannWhitneyU(x, y);
            assertEquals(9.0, U, 0.0001);
        } catch (NullArgumentException | NoDataException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        double Umin = 9.0;
        int n1 = 3;
        int n2 = 3;
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
            assertEquals(0.1909, pValue, 0.0001);
        } catch (ConvergenceException | MaxCountExceededException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double pValue = test.mannWhitneyUTest(x, y);
            assertEquals(0.3818, pValue, 0.0001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            e.printStackTrace();
        }
    }
}
