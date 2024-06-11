import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver_basic() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.5, 2.5);
        assertEquals(0.0, Precision.round(result, 5));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBisectionSolver_unsupportedOperation() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolver_maxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / (x + 0.01));
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        solver.solve(function, -1.0, 1.0);
    }

    @Test
    public void testBisectionSolver_intervalOutOfOrder() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, -1.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testBisectionSolver_largeInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -1000.0, 1000.0);
        assertTrue(Precision.isClose(result, 0.0, 1E-6));
    }

    @Test
    public void testBisectionSolver_smallInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -1.0e-7, 1.0e-7);
        assertTrue(Precision.isClose(result, 0.0, 1E-9));
    }

    @Test
    public void testBisectionSolver_functionThrowsNumberIsTooSmallException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NumberIsTooSmallException {
                if (x < 1E-10) {
                    throw new NumberIsTooSmallException();
                }
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }
}