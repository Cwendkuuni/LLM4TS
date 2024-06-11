import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction function = new Function("x^2-2", x -> x * x - 2.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.41421356237; // Approximate value of sqrt(2)
         try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1);
            assertEquals(expectedResult, result, 0.001); // Tolerance
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithCustomSolver() {
        UnivariateRealFunction function = new Function("x^2-2", x -> x * x - 2.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        UnivariateRealSolver solver = new BrentSolver();
        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy, solver);
            assertEquals(expectedResult, result, 0.001); // Tolerance
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new Function("x^2-2", x -> x * x - 2.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
         try {
            double[] bracketedValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            double a = bracketedValues[0];
            double b = bracketedValues[1];
            double expectedA = 0.5;
            double expectedB = 1.5;
            assertEquals(expectedA, a, 0.001); // Tolerance
            assertEquals(expectedB, b, 0.001); // Tolerance
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction function = new Function("x^2-2", x -> x * x - 2.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 5;
         try {
            double[] bracketedValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            double a = bracketedValues[0];
            double b = bracketedValues[1];
            double expectedA = 0.5;
            double expectedB = 1.5;
            assertEquals(expectedA, a, 0.001); // Tolerance
            assertEquals(expectedB, b, 0.001); // Tolerance
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFailedToBracket() {
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 1.0;
         try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            fail("Unexpected result");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    private static UnivariateRealFunction newFunction(String expression, FunctionEvaluator evaluator) {
        return new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return evaluator.evaluate(expression, x);
            }
        };
    }

    private static class FunctionEvaluator {
        private final String expression;

        public FunctionEvaluator(String expression) {
            this.expression = expression;
        }

        public double evaluate(String expression, double x) {
            return new Expressions().eval(expression, new Context(Double.class, x));
        }
    }

    private static class Function {
        private final String expression;

        public Function(String expression, FunctionEvaluator evaluator) {
            this.expression = expression;
            this.evaluator = evaluator;
        }

        public double value(double x) {
            return evaluator.evaluate(expression, x);
        }

        private final FunctionEvaluator evaluator;
    }
}