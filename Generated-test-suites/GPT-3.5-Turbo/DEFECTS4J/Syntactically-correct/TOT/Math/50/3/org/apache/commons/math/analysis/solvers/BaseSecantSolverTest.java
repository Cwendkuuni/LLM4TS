import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_ExactRoot_1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };

        double result = solver.solve(100, f, 0, 2);

        assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void testSolve_ExactRoot_2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        double result = solver.solve(100, f, -10, 10);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testSolve_DifferentSigns() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double result = solver.solve(100, f, 0, 10);

        assertEquals(Math.PI, result, EPSILON);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sqrt(x);
            }
        };

        solver.solve(100, f, 0, 10);
    }

    @Test
    public void testSolve_Accuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x;
            }
        };

        double result = solver.solve(100, f, -10, 10);

        assertEquals(0.0, result, EPSILON);
    }

}