import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-10;

    // Helper function to check if two doubles are equal within a small epsilon
    private boolean isClose(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    @Test
    public void testSolve_constantFunction() {
        UnivariateRealFunction constantFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 5.0;
            }
        };

        BisectionSolver solver = new BisectionSolver(constantFunction);
        try {
            double result = solver.solve(-10, 10);
            // The function is constant, so the result should be in the input interval
            assertEquals(result, 0, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolve_linearFunction() {
        UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 5;
            }
        };

        BisectionSolver solver = new BisectionSolver(linearFunction);
        try {
            double result = solver.solve(-10, 10);
            // The function is a straight line, so the result should be -2.5
            assertEquals(result, -2.5, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolve_squareRootFunction() {
        UnivariateRealFunction squareRootFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sqrt(x);
            }
        };

        BisectionSolver solver = new BisectionSolver(squareRootFunction);
        try {
            double result = solver.solve(0, 10);
            // The function is a square root, so the result should be close to 3
            assertEquals(result, 3, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction maxIterationsExceededFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                // This function never converges
                return x;
            }
        };

        BisectionSolver solver = new BisectionSolver(maxIterationsExceededFunction);
        solver.solve(-10, 10);
        fail("Exception not thrown");
    }

    @Test
    public void testSolve_edgeCases() {
        UnivariateRealFunction edgeCasesFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    // Division by zero error
                    throw new FunctionEvaluationException(x);
                }
                return 1 / x;
            }
        };

        BisectionSolver solver = new BisectionSolver(edgeCasesFunction);
        try {
            double result = solver.solve(-10, 10);
            // The function has a singularity at x = 0, so the result should be close to 0
            assertEquals(result, 0, EPSILON);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception thrown");
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded exception thrown");
        }
    }

}