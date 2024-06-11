import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        // Create a test UnivariateRealFunction
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver);
    }

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, 1, 3, 2.5);
            assertEquals(2, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, 1, 3);
            assertEquals(2, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInvalidInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 3, 1);
            fail("Exception not thrown for invalid interval");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 1, 3, 2.5);
            fail("Exception not thrown for exceeding max iterations");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testMidpointCase() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, -2, 2);
            assertEquals(2, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }
}