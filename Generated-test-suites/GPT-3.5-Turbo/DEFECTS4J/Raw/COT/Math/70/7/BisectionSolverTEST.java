import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.util.MathUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve_minAndMaxEqual() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };

        try {
            double result = solver.solve(f, 2.0, 2.0);
            assertEquals(2.0, result, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testSolve_minAndMaxBracketRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };

        try {
            double result = solver.solve(f, 0.0, 2.0);
            assertEquals(1.0, result, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testSolve_minAndMaxDoNotBracketRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };

        try {
            double result = solver.solve(f, 0.0, 0.5);
            fail("Expected MaxIterationsExceededException but got result: " + result);
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testSolve_fThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        try {
            double result = solver.solve(f, 0.0, 1.0);
            fail("Expected FunctionEvaluationException but got result: " + result);
        } catch (FunctionEvaluationException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testSolve_minGreaterThanOrEqualToMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };

        try {
            double result = solver.solve(f, 2.0, 1.0);
            fail("Expected IllegalArgumentException but got result: " + result);
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

}