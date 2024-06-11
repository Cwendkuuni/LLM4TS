import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                // Define the function to be used for testing
                return Math.pow(x, 2) - x - 2;
            }
        };
    }

    @Test
    public void testSolve_MinGreaterThanOrEqualMax() {
        try {
            solver.solve(function, 2, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Success
        }
    }

    @Test
    public void testSolve_FunctionNotBracketed() {
        try {
            solver.solve(function, 1, 2);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Success
        }
    }

    @Test
    public void testSolve_FunctionBracketed() {
        try {
            double result = solver.solve(function, 1, 3);
            assertEquals(2.0001220703125, result, 1E-6);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected MaxIterationsExceededException");
        }
    }

    @Test
    public void testSolve_AbsoluteAccuracyReached() {
        try {
            double result = solver.solve(function, 1, 3, 2.5);
            assertEquals(2.0001220703125, result, 1E-6);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected MaxIterationsExceededException");
        }
    }

    @Test
    public void testSolve_AbsoluteAccuracyNotReached() {
        try {
            solver.solve(function, 1, 3, 2.5);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Success
        }
    }

}