import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    // Test constructor with function parameter
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        assertEquals(f, solver.getFunction());
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0);
    }

    // Test constructor without function parameter
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();

        assertNull(solver.getFunction());
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0);
    }

    // Test solve method with min, max, and initial parameters (deprecated)
    @Test
    public void testSolveWithMinMaxInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        assertEquals(2, solver.solve(1, 3, 2), 1E-6);
    }

    // Test solve method with min and max parameters (deprecated)
    @Test
    public void testSolveWithMinMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        assertEquals(2, solver.solve(1, 3), 1E-6);
    }

    // Test solve method with function, min, and max parameters
    @Test
    public void testSolveWithFunctionMinMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertEquals(2, solver.solve(f, 1, 3), 1E-6);
    }

    // Test solve method with function, min, max, and initial parameters
    @Test
    public void testSolveWithFunctionMinMaxInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertEquals(2, solver.solve(f, 1, 3, 2), 1E-6);
    }

    // Test solve method with valid range
    @Test
    public void testSolveValidRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertEquals(2, solver.solve(f, 1, 3), 1E-6);
    }

    // Test solve method with invalid range
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, 3, 1);
    }

    // Test solve method with max iterations exceeded
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, 1, 3, 2);
    }
}