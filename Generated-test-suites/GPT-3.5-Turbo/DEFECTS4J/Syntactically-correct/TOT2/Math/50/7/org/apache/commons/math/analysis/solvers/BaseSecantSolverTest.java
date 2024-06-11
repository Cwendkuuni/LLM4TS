import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolveForDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver.Default();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            // Define function for testing
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 5);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolveForRelativeAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver.RelativeAccuracy(1e-8);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            // Define function for testing
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 5);
        assertEquals(2.0, root, 1e-8);
    }

    @Test
    public void testSolveForAbsoluteAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver.AbsoluteAccuracy(1e-10);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            // Define function for testing
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 5);
        assertEquals(2.0, root, 1e-10);
    }

    @Test
    public void testSolveForFunctionValueAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver.FunctionValueAccuracy(1e-12);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            // Define function for testing
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 5);
        assertEquals(2.0, root, 1e-12);
    }

    @Test
    public void testSolveForBeyondBoundaries() {
        BaseSecantSolver solver = new BaseSecantSolver.Default();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            // Define function for testing
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 1);
        assertEquals(Double.NaN, root, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        BaseSecantSolver.Method invalidMethod = null;
        BaseSecantSolver solver = new BaseSecantSolver.Default(invalidMethod);
    }
}