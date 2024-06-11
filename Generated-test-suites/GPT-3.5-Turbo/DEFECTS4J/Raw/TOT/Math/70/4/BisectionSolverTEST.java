import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test a typical use case where the root is between min and max
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // Root is at x = 1
            }
        };
        BisectionSolver solver1 = new BisectionSolver();
        double root1 = solver1.solve(f1, 0, 2);
        Assert.assertEquals(1, root1, 1E-6);

        // Test an edge case where the root is at the minimum value
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x; // Root is at x = 0
            }
        };
        BisectionSolver solver2 = new BisectionSolver();
        double root2 = solver2.solve(f2, 0, 1);
        Assert.assertEquals(0, root2, 1E-6);

        // Test an edge case where the root is at the maximum value
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // Root is at x = 1
            }
        };
        BisectionSolver solver3 = new BisectionSolver();
        double root3 = solver3.solve(f3, -1, 1);
        Assert.assertEquals(1, root3, 1E-6);

        // Test an error scenario where the maximum number of iterations is exceeded
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x; // No root
            }
        };
        BisectionSolver solver4 = new BisectionSolver();
        try {
            double root4 = solver4.solve(f4, 0, 1);
            Assert.fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Test passed
        }
    }
}