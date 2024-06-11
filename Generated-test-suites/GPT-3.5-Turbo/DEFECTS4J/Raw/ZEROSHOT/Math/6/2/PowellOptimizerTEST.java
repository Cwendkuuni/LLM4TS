import org.junit.Test;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

public class PowellOptimizerTest {
    
    // Test if the PowellOptimizer constructor throws a NotStrictlyPositiveException when abs <= 0
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorAbsNegative() {
        PowellOptimizer optimizer = new PowellOptimizer(1, -1);
    }
    
    // Test if the PowellOptimizer constructor throws a NumberIsTooSmallException when rel < 2 * Math.ulp(1d)
    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructorRelNegative() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 1);
    }   

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.1);
        double[] startPoint = {0, 0};
        double[] result = optimizer.doOptimize();
        assertArrayEquals(new double[]{0, 0}, result, 0.0001);
    }

    private void assertArrayEquals(double[] expected, double[] actual, double delta) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], delta);
        }
    }
}