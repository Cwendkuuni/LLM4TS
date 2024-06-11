import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Method stub for test purposes
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve_WhenRootBetweenMinAndMax_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 3);

        assertEquals(2, root, 1e-6);
    }

    @Test
    public void testSolve_WhenRootAtMin_ReturnsMin() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 2);

        assertEquals(0, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenRootAtMax_ReturnsMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 1, 2);

        assertEquals(2, root, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WhenInvalidMaxEval_ReturnsIllegalArgumentException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        solver.solve(0, f, 0, 3);
    }

    @Test
    public void testSolve_WhenFunctionValueAccuracyIsMet_ReturnsApproximation() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Method stub for test purposes
                return x * x - 4;
            }
        };
        solver.setFunctionValueAccuracy(1e-4);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 3);

        assertEquals(2, root, 1e-4);
    }

    @Test
    public void testSolve_WhenRelativeAccuracyIsMet_ReturnsApproximation() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Method stub for test purposes
                return x * x - 4;
            }
        };
        solver.setRelativeAccuracy(1e-4);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 3);

        assertEquals(2, root, 1e-4);
    }

    @Test
    public void testSolve_WhenAbsoluteAccuracyIsMet_ReturnsApproximation() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Method stub for test purposes
                return x * x - 4;
            }
        };
        solver.setAbsoluteAccuracy(1e-4);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 3);

        assertEquals(2, root, 1e-4);
    }

}