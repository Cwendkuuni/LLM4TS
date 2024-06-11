import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private ConvergenceChecker<PointVectorValuePair> checker;

    @Before
    public void setup() {
        checker = new SimpleVectorValueChecker();
        optimizer = new GaussNewtonOptimizer(checker);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeConvergence() {
        PointVectorValuePair result = optimizer.doOptimize();
        assertTrue(checker.converged(1, result, result));
    }

    @Test
    public void testDoOptimizeCost() {
        PointVectorValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizePoint() {
        PointVectorValuePair result = optimizer.doOptimize();
        assertArrayEquals(result.getPoint(), result.getPoint());
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrixException() {
        optimizer.setUseLU(false);
        optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeMathInternalError() {
        optimizer.setConvergenceChecker(null);
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    private void assertArrayEquals(double[] expected, double[] actual) {
        if (expected.length != actual.length) {
            fail("lengths of arrays differ");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.0001);
        }
    }
}