import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-9;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver() {
            @Override
            protected double doSolve() {
                return 0;
            }

            @Override
            protected double computeObjectiveValue(double x) {
                return 0;
            }
        };
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        double result = solver.solve(100, f, min, max, startValue);
        assertEquals(0, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        double result = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(0, result, EPSILON);
    }

    @Test
    public void testSolveWithMaxEval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        int maxEval = 50;
        double result = solver.solve(maxEval, f, min, max, startValue);
        assertEquals(0, result, EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        solver.solve(1, f, min, max, startValue);
    }
}