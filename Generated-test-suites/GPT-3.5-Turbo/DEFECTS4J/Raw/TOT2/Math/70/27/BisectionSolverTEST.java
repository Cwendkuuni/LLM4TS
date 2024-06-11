import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSolveWithInitial() {
        try {
            BisectionSolver solver = new BisectionSolver();
            double result = solver.solve(Math::cos, 0.0, 2.0, 1.0);
            assertEquals(1.5707963267948966, result, 1E-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        try {
            BisectionSolver solver = new BisectionSolver();
            double result = solver.solve(Math::cos, 0.5, 2.0);
            assertEquals(1.5707963267948966, result, 1E-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        solver.solve(Math::cos, 0.0, 2.0, 1.0);
    }

    @Test
    public void testFunctionEvaluationException() {
        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(x -> {
                if (x == 1.0) {
                    throw new FunctionEvaluationException(x);
                }
                return Math.cos(x);
            }, 0.5, 2.0);
            fail("Expected FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidInterval() {
        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(Math::cos, 2.0, 0.5);
            fail("No Exception thrown for invalid interval");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

}