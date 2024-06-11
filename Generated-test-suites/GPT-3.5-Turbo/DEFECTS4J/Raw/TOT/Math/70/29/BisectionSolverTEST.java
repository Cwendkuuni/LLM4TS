
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve_WithValidFunction_ShouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 0, 3);
        
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testSolve_WithInvalidFunction_ShouldThrowFunctionEvaluationException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                // Divide by zero to create an invalid function
                return 1 / (x - 1);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(f, 0, 3);
        });
    }

    @Test
    public void testSolve_WithMaxIterationsExceeded_ShouldThrowMaxIterationsExceededException() throws FunctionEvaluationException {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(5);
        
        assertThrows(MaxIterationsExceededException.class, () -> {
            solver.solve(f, 0, 5);
        });
    }

    @Test
    public void testSolve_WithNegativeInterval_ShouldThrowIllegalArgumentException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        
        assertThrows(IllegalArgumentException.class, () -> {
            solver.solve(f, -3, -1);
        });
    }

    @Test
    public void testSolve_WithInvalidIntervalOrder_ShouldThrowIllegalArgumentException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        
        assertThrows(IllegalArgumentException.class, () -> {
            solver.solve(f, 3, 1);
        });
    }

    @Test
    public void testSolve_WithEqualIntervalBounds_ShouldReturnCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 2, 2);
        
        assertEquals(2.0, result, EPSILON);
    }

}
