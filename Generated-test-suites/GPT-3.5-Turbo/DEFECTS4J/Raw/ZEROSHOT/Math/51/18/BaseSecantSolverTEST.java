import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_AnySide_ValidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        double expected = 1;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }
    
    @Test
    public void testSolve_AnySide_InvalidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 5;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        double expected = Double.NaN;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_LeftSide_ValidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;
        double expected = 0.9999990463256836;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_LeftSide_InvalidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 5;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;
        double expected = Double.NaN;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_RightSide_ValidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;
        double expected = 1.0000019073486328;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_RightSide_InvalidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 10;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;
        double expected = Double.NaN;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_BelowSide_ValidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;
        double expected = 0.9999990463256836;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_BelowSide_InvalidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 10;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;
        double expected = Double.NaN;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_AboveSide_ValidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;
        double expected = 1.0000019073486328;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_AboveSide_InvalidInputs() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1; // Linear function x - 1
            }
        };
        double min = 5;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;
        double expected = Double.NaN;
        double actual = solver.solve(100, f, min, max, startValue, allowedSolution);
        assertEquals(expected, actual, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

}