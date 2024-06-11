import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 2, 0);
            assertEquals(2.0, result, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolveWithMinMax() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 2);
            assertEquals(2.0, result, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolveWithMinMaxFail() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, -2);
            // Test fails as initial interval does not bracket the root
            assertEquals(Double.NaN, result, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

}