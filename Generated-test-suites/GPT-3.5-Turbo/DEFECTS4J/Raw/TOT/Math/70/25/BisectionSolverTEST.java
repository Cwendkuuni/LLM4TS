import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double solution = solver.solve(function, 0, 3);
            Assert.assertEquals(2.0, solution, 1E-6);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithMinMax() {
        BisectionSolver solver = new BisectionSolver();
        double min = 1.0;
        double max = 3.0;

        try {
            double solution = solver.solve(min, max);
            Assert.assertEquals(2.0, solution, 1E-6);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        solver.solve(function, -100, 100);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Exception in function evaluation");
            }
        };

        solver.solve(function, -100, 100);
    }

    @Test
    public void testSolveCompleteAccuracy() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        try {
            double solution = solver.solve(function, 0, 3, 1E-12);
            Assert.assertEquals(1.0, solution, 1E-12);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

}