import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsiMethod() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };

        double root = solver.solve(100, function, -10.0, 10.0);
        assertEquals("Root found is not correct", 0.0, root, 1e-6);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolver_RegulaFalsiMaxEval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(1, function, min, max, 0.0);
            }
        };

        solver.solve(1, function, -10.0, 10.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolver_RegulaFalsiNoBracketing() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(100, f, min, max, 0.0);
            }
        };

        solver.solve(100, function, -10.0, 10.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolver_RegulaFalsiTooSmall() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(100, f, min, max, 0.0);
            }
        };

        solver.setAbsoluteAccuracy(1e-12);
        solver.solve(100, function, -10.0, 10.0);
    }

    @Test
    public void testSolver_IllinoisMethod() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(100, f, min, max, 0.0);
            }
        };

        double root = solver.solve(100, function, -10.0, 10.0);
        assertEquals("Root found is not correct", 0.0, root, 1e-6);
    }

    @Test
    public void testSolver_PegasusMethod() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.PEGASUS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(100, f, min, max, 0.0);
            }
        };

        double root = solver.solve(100, function, -10.0, 10.0);
        assertEquals("Root found is not correct", 0.0, root, 1e-6);
    }
}