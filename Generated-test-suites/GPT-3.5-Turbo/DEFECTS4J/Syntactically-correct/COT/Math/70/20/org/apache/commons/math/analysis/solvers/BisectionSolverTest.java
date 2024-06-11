import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    // Method: BisectionSolver(UnivariateRealFunction f)
    @Test
    public void testConstructor1() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    // Method: BisectionSolver()
    @Test
    public void testConstructor2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    // Method: solve(double min, double max, double initial)
    @Test
    public void testSolve1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double solution = solver.solve(-10, 10, 0);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve(double min, double max)
    @Test
    public void testSolve2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double solution = solver.solve(-10, 10);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve(UnivariateRealFunction f, double min, double max, double initial)
    @Test
    public void testSolve3() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(f, -10, 10, 0);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve(UnivariateRealFunction f, double min, double max)
    @Test
    public void testSolve4() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(f, -10, 10);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve
    // Edge case: no root within the interval
    @Test(expected = FunctionEvaluationException.class)
    public void testSolve5() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(-10, 10);
    }

    // Method: solve
    // Edge case: initial value already a root
    @Test
    public void testSolve6() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double solution = solver.solve(-10, 10, 2);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve
    // Edge case: interval size equal to absolute accuracy
    @Test
    public void testSolve7() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double solution = solver.solve(-2, 2);
        assertEquals(2.0, solution, 0.0001);
    }

    // Method: solve
    // Exception: MaxIterationsExceededException
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve8() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(-10, 10, 0);
    }
}