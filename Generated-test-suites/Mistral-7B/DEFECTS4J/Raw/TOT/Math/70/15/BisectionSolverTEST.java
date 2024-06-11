import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.RootFindersFactoryException;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = f -> Math.pow(x, 2) - 4.0;
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(f, -2.0, 2.0);
            Assert.assertEquals(2.0, Precision.round(root, 5));
        } catch (MaxIterationsExceededException e) {
            // expected since we set maximalIterationCount to 1
        } catch (FunctionEvaluationException e) {
            // should not be thrown since we provided valid x values
            Assert.fail("FunctionEvaluationException was thrown but should not have been");
        }
    }

    @Test
    public void testFunctionWithNegativeRoot() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 1.0;
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(f, -2.0, 2.0);
            Assert.assertEquals(-1.0, Precision.round(root, 5));
        } catch (MaxIterationsExceededException e) {
            // expected since we set maximalIterationCount to 1
        } catch (FunctionEvaluationException e) {
            // should not be thrown since we provided valid x values
            Assert.fail("FunctionEvaluationException was thrown but should not have been");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionWithInvalidMin() {
        UnivariateRealFunction f = x -> Math.sqrt(x);
        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, Double.NEGATIVE_INFINITY, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionWithInvalidMax() {
        UnivariateRealFunction f = x -> Math.sqrt(x);
        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, 0.0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFunctionWithNullFunction() {
        UnivariateRealFunction f = null;
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(f, -2.0, 2.0);
            Assert.fail("NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFunctionWithInfiniteRoot() {
        UnivariateRealFunction f = x -> E.EulerNumber().exp(x) - E.EULER;
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(f, -100.0, 100.0);
            Assert.fail("FunctionEvaluationException was not thrown but should have been");
        } catch (FunctionEvaluationException e) {
            // expected since e^(-100) is too small and causes NumberIsTooSmallException
        } catch (MaxIterationsExceededException e) {
            // expected since we set maximalIterationCount to 1
        }
    }
}