import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-15;

    @Test
    public void testSolve_withMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0);

        assertEquals(1.0, solution, EPSILON);
    }

    @Test
    public void testSolve_withMaxEvalAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0, 1.0);

        assertEquals(1.0, solution, EPSILON);
    }

    @Test
    public void testSolve_withMaxEvalAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(1.0, solution, EPSILON);
    }

    @Test
    public void testSolve_withMaxEvalAndStartValueAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(1.0, solution, EPSILON);
    }
    
    @Test
    public void testSolve_withMaxEvalAndStartValueAndAllowedSolution_AND_SOLUTION_1() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0, 0.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(1.0, solution, EPSILON);
    }

    @Test(expected = MathInternalError.class)
    public void testException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double solution = solver.solve(100, f, 0.0, 2.0, 3.0);
    }
}