import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunctionParam() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        try {
            double result = solver.solve(f, 1.0, 3.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSolveWithMinMaxParams() {
        BisectionSolver solver = new BisectionSolver();
        
        try {
            double result = solver.solve(1.0, 3.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSolveWithMinMaxInitialParams() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        try {
            double result = solver.solve(f, 1.0, 3.0, 2.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSolveInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        try {
            solver.solve(f, 3.0, 1.0); // Invalid interval
            fail("Exception expected");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Exception expected
        }
    }

    @Test
    public void testSolveMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
        
        try {
            solver.solve(f, 1.0, 3.0); // Max iterations exceeded
            fail("Exception expected");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Exception expected
        }
    }
}