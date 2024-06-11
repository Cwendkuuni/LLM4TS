import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-10;
    private BisectionSolver solver;
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve_WithContinuousFunction_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        try {
            double solution = solver.solve(f, 0, 4); // root is 2
            assertEquals(2.0, solution, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolve_WithNoRootInInterval_ThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x); // f(x) = sin(x)
            }
        };
        try {
            solver.solve(f, 0, 1); // no root in interval [0, 1]
            fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            // expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolve_WithLinearFunction_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 3; // f(x) = x - 3
            }
        };
        try {
            double solution = solver.solve(f, 0, 5); // root is 3
            assertEquals(3.0, solution, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}