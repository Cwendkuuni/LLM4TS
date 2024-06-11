import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
    }

    @Test
    public void testGetInitialStep() {
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testSetInitialStepWithNegativeValue() {
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testFindUpperBound() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double a = 0.0;
        double h = 1.0;
        double b = optimizer.findUpperBound(f, a, h);
        assertEquals(3.0, b, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundThrowsException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        double a = 0.0;
        double h = 1.0;
        optimizer.findUpperBound(f, a, h);
    }

    @Test
    public void testIdentityPreconditionerPrecondition() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {4.0, 5.0, 6.0};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 0.0);
    }

    @Test
    public void testLineSearchFunctionValue() {
        double[] searchDirection = {1.0, 2.0, 3.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction = optimizer.new LineSearchFunction(searchDirection);
        double x = 2.0;
        double value = lineSearchFunction.value(x);
        assertEquals(20.0, value, 0.0);
    }

}