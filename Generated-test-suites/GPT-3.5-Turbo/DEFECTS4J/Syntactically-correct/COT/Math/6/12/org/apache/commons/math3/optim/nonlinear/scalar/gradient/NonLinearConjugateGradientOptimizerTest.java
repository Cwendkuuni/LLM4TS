import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.1);
        double[] startPoint = {0.0};
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };
        optimizer.optimize(startPoint, GoalType.MINIMIZE, objective);
        double[] expectedPoint = {2.0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        PointValuePair actual = optimizer.getOptimum();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoOptimizeWithMaxEvaluations() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.1);
        double[] startPoint = {0.0};
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(startPoint, GoalType.MINIMIZE, objective);
        double[] expectedPoint = {2.0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        PointValuePair actual = optimizer.getOptimum();
        assertEquals(expected, actual);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoOptimizeException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.1);
        double[] startPoint = {0.0};
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };
        optimizer.optimize(startPoint, null, objective);
        double[] expectedPoint = {2.0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        PointValuePair actual = optimizer.getOptimum();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };
        double a = 0.0;
        double h = 0.5;
        double expected = 1.0;
        double actual = optimizer.findUpperBound(f, a, h);
        assertEquals(expected, actual, 1e-15);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFindUpperBoundException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };
        double a = 1.0;
        double h = 0.5;
        double actual = optimizer.findUpperBound(f, a, h);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {2.0, 4.0, 6.0};
        double[] expected = {2.0, 4.0, 6.0};
        double[] actual = preconditioner.precondition(variables, r);
        assertArrayEquals(expected, actual, 1e-15);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        double[] searchDirection = {1.0, 2.0, 3.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction = new NonLinearConjugateGradientOptimizer.LineSearchFunction(searchDirection);
        double x = 2.0;
        double expected = 20.0;
        double actual = lineSearchFunction.value(x);
        assertEquals(expected, actual, 1e-15);
    }
}