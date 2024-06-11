import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        double result = solver.solve(100, f, min, max, startValue);
        assertEquals(2.0, result, absoluteAccuracy);
   }

    @Test
    public void testSolveWithAllowedSolution() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        double result = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(2.0, result, absoluteAccuracy);
    }

    @Test
    public void testSolveWithMaxEval() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = 0.0;
        double max = 10.0;
        double startValue = 1.0;
        int maxEval = 100;
        double result = solver.solve(maxEval, f, min, max, startValue);
        assertEquals(2.0, result, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = 0.0;
        double max = 10.0;
        double startValue = -1.0;
        solver.solve(100, f, min, max, startValue);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        double result = solver.solve(100, f, min, max, startValue);
        assertEquals(2.0, result, absoluteAccuracy);
   }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
             public double value(double x) {
                return x * x - 4;
             }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        double result = solver.solve(100, f, min, max, startValue);
        assertEquals(2.0, result, absoluteAccuracy);
   }
}