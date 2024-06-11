import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    private static final double DEFAULT_RELATIVE_THRESHOLD = 0.0001;
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 1e-6;
    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp(1d);

    // Tests for the constructor

    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructorInvalidRelativeThreshold() {
        new BrentOptimizer(MIN_RELATIVE_TOLERANCE - 1,DEFAULT_ABSOLUTE_THRESHOLD);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbsoluteThreshold() {
        new BrentOptimizer(DEFAULT_RELATIVE_THRESHOLD, -DEFAULT_ABSOLUTE_THRESHOLD);
    }

    // Tests for the doOptimize() method

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_THRESHOLD,
                                                      DEFAULT_ABSOLUTE_THRESHOLD);
        double[] coeffs = { -20, 1, -2, 3 };

        // Create a univariate differentiable function
        // using a polynomial function
        double min = 2.0;
        double max = 4.0;
        PolynomialFunction f = new PolynomialFunction(coeffs);

        // Test with optimization target as minimum
        double minimum = optimizer.optimize(1000, f, GoalType.MINIMIZE, min, max).getPoint();
        assertEquals("Test with optimization target as minimum", 2.5, minimum, Precision.EPSILON);

        // Test with optimization target as maximum
        double maximum = optimizer.optimize(1000, f, GoalType.MAXIMIZE, min, max).getPoint();
        assertEquals("Test with optimization target as maximum", 3.5, maximum, Precision.EPSILON);
    }

    // Additional tests could be added for testing other methods in the BrentOptimizer class

}