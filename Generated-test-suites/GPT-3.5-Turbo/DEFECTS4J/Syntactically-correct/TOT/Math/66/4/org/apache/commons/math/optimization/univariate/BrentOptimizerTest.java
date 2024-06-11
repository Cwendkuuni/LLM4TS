import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-12;
    private static final int MAX_ITERATIONS = 1000;
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNotStrictlyPositiveEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNotStrictlyPositiveT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5, 0.0);
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testLocalMinGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return -x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testLocalMinWithTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5, EPSILON, EPSILON);
        assertEquals(0.0, result, EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinMaximumIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5, EPSILON, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testLocalMinFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
    }

    @Test
    public void testLocalMinBoundsSwapped() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, -1.0);
        assertEquals(0.0, result, EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testLocalMinNotStrictlyIncreasingFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
    }

    @Test
    public void testLocalMinFunctionValueOutsideBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 2.0);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testLocalMinFunctionValueOnLowerBound() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, -1.0);
        assertEquals(-1.0, result, EPSILON);
    }

    @Test
    public void testLocalMinFunctionValueOnUpperBound() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 1.0);
        assertEquals(1.0, result, EPSILON);
    }

}