import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;


public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-8, 1e-4);
    }

    @Test(expected = NotStrictlyPositiveException.class) 
    public void constructorThrowsExceptionForNonPositiveAbs() {
        optimizer = new BrentOptimizer(1e-8, 0);
    }

    @Test(expected = NumberIsTooSmallException.class) 
    public void constructorThrowsExceptionForSmallRel() {
        optimizer = new BrentOptimizer(1e-10, 1e-4);
    }

    @Test
    public void testDoOptimize() {
        double[] expected = {0};
        double[] actual = optimizer.doOptimize();
        assertArrayEquals(expected, actual, 1e-8);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertTrue(result == a);

        result = optimizer.best(b, a, true);
        assertTrue(result == a);

        result = optimizer.best(b, c, true);
        assertTrue(result == b);

        result = optimizer.best(b, c, false);
        assertTrue(result == c);
    }
}