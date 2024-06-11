import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxEvaluations(1000);
    }

    @Test
    public void testDoOptimize() {
        // Test case 1: Minimumimization goal type
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair expected = new PointValuePair(new double[] { 0.0 }, 0.0);
        PointValuePair result = optimizer.optimize(1, objectiveFunction, goalType, 0, 2);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-15);
        assertEquals(expected.getValue(), result.getValue(), 1e-15);

        // Test case 2: Maximization goal type
        goalType = GoalType.MAXIMIZE;
        objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return -x * x;
            }
        };
        expected = new PointValuePair(new double[] { 0.0 }, 0.0);
        result = optimizer.optimize(1, objectiveFunction, goalType, 0, 2);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-15);
        assertEquals(expected.getValue(), result.getValue(), 1e-15);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double upperBound = optimizer.findUpperBound(f, 0, 1);
        assertEquals(2.0, upperBound, 1e-15);
        
        // When function values do not have opposite signs, an exception should be thrown
        f = new UnivariateFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        optimizer.findUpperBound(f, 0, 1);
    }

    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = new double[] {1.0, 0.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lsf = optimizer.new LineSearchFunction(searchDirection);
        
        // Test when the dot product is zero
        UnivariateFunction lineSearchFunction = new UnivariateFunction() {
            public double value(double x) {
                return 2 * x;
            }
        };
        double result = lsf.value(0.5);
        assertEquals(0.0, result, 1e-15);
        
        // Test when the dot product is negative
        lineSearchFunction = new UnivariateFunction() {
            public double value(double x) {
                return 2 * x + 1;
            }
        };
        result = lsf.value(0.5);
        assertTrue(result < 0.0);
        
        // Test when the dot product is positive
        lineSearchFunction = new UnivariateFunction() {
            public double value(double x) {
                return -2 * x + 1;
            }
        };
        result = lsf.value(0.5);
        assertTrue(result > 0.0);
    }
}