import org.apache.commons.math.analysis.UnivariateRealFunction;

import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(f, 1, 3, 2);
        assertEquals(2, root, 1E-6);
    }

    @Test
    public void testSolveWithoutInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(f, 1, 3);
        assertEquals(2, root, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver.solve(f, 0, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation exception");
            }
        };
        solver.solve(f, 1, 3);
    }

    @Test
    public void testSolveNegativeRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return -x * x + 4;
            }
        };
        double root = solver.solve(f, -3, -1);
        assertEquals(-2, root, 1E-6);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(1E-12);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(f, 1, 3);
        assertEquals(2, root, 1E-12);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        BisectionSolver solver = new BisectionSolver();
        solver.setRelativeAccuracy(1E-6);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(f, 1, 3);
        assertEquals(2, root, 1E-6);
    }
}