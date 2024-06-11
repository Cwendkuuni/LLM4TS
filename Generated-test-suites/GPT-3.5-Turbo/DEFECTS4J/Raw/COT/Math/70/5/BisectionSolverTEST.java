
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {
    
    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        try {
            double result = solver.solve(f, 0, 2, 1.5);
            assertEquals(1.0, result, 0.0001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolveWithoutInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        try {
            double result = solver.solve(f, 0, 2);
            assertEquals(1.0, result, 0.0001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        solver.setMaximalIterationCount(5);
        solver.solve(f, 0, 10);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Error evaluating function");
            }
        };
        solver.solve(f, 0, 2);
    }
}
