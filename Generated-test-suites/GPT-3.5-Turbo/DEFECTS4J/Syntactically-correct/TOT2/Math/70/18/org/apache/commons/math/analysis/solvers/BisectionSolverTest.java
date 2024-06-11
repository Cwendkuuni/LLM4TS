import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitialValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithoutInitialValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 3.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(5);
        solver.solve(f, 1.0, 3.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation exception");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 1.0, 3.0);
    }
}