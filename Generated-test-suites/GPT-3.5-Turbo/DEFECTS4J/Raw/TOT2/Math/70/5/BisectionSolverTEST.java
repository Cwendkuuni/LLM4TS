import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    @Test
    public void testSolveMinMaxInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(0, 10, 1);
            assertEquals(5.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolveMinMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(0, 10);
            assertEquals(5.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolveFunctionMinMaxInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, 0, 10, 1);
            assertEquals(5.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolveFunctionMinMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, 0, 10);
            assertEquals(5.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }
}