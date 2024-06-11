import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMinMax() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 3);
        assertNotEquals(0.0, result);
        assertNotEquals(3.0, result);
    }

    @Test
    public void testSolveWithInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0, 3, 2);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(0, 3);
    }

    @Test
    public void testEdgeCase() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -5, 5);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0, 3);
    }

}