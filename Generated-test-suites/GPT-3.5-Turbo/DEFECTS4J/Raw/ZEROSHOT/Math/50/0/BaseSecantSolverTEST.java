import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Create a test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create the solver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving for root
        double result = solver.solve(100, function, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // Create a test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create the solver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving for a specific allowed solution
        double result = solver.solve(100, function, 1, 3, 2.1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Create a test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create the solver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving with maximum number of evaluations
        double result = solver.solve(3, function, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithMaxEvalAndAllowedSolution() {
        // Create a test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create the solver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving with maximum number of evaluations and allowed solution
        double result = solver.solve(3, function, 1, 3, 2.1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }
}