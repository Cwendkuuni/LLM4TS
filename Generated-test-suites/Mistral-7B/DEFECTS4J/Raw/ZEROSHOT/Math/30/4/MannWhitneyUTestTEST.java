import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mwt = new MannWhitneyUTest();

        // Test with default constructor
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0};
        double result1 = mwt.mannWhitneyU(x1, y1);
        assertEquals(6.0, result1, 0.001);

        double[] x2 = {};
        double[] y2 = {};
        try {
            mwt.mannWhitneyU(x2, y2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        try {
            mwt.mannWhitneyU(null, y2);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            mwt.mannWhitneyU(x1, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            mwt.mannWhitneyUTest(x1, y1);
            fail("Expected p-value");
        } catch (Exception e) {
            // Expected
        }

        // Test with custom constructor
        MannWhitneyUTest mwtCustom = new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN);
        double[] x3 = {1.0, 2.0, 2.0, 3.0};
        double[] y3 = {4.0, 4.0, 4.0, 5.0};
        double result2 = mwtCustom.mannWhitneyU(x3, y3);
        assertEquals(8.0, result2, 0.001);
    }

    @Test
    public void testAsymptoticPValue() throws Exception {
        MannWhitneyUTest mwt = new MannWhitneyUTest();

        double Umin = 5.0;
        int n1 = 5;
        int n2 = 3;
        double pValue = mwt.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0115653, pValue, 0.00001);

        Umin = 1.0;
        pValue = mwt.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.988435, pValue, 0.00001);
    }
}