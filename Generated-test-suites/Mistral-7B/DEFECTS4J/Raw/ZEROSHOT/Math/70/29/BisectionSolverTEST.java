import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolverWithValidFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(function, -3.0, 3.0);
            Assert.assertEquals(0.0, root, 1e-6);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Max iterations should not be exceeded");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not fail");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithFunctionLessThanMinimalIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(1);
        solver.solve(function, -3.0, 3.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithFunctionWithZeroAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver(100, 0.0);
        solver.solve(function, -4.0, 4.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolverWithFunctionOutsideInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolverWithFunctionThrowsException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation failed");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }
}