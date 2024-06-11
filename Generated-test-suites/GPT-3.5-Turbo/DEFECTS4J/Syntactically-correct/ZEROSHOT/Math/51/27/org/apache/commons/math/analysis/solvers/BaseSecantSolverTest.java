import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        
        // test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver.solve(100, f1, 0, 4);
        Assert.assertEquals(2.0, result1, 0.0001);
        
        // test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 9;
            }
        };
        double result2 = solver.solve(100, f2, -3, 3);
        Assert.assertEquals(3.0, result2, 0.0001);
    }
}