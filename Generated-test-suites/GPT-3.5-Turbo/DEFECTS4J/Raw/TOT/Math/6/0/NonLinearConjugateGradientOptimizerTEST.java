import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize_minimizeObjectiveValue() {
        // Create an instance of NonLinearConjugateGradientOptimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );

        // Define the objective function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 5, 2);
            }
        };

        // Set the objective function
        optimizer.setObjectiveFunction(objectiveFunction);

        // Set the initial guess
        optimizer.setStartPoint(new double[]{0.0});

        // Set the goal type to minimize
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Optimize the objective function
        PointValuePair result = optimizer.optimize();

        // Verify that the optimal point is close to the minimum of the objective function
        assertEquals(5.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize_maximizeObjectiveValue() {
        // Create an instance of NonLinearConjugateGradientOptimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );

        // Define the objective function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x - 5, 2);
            }
        };

        // Set the objective function
        optimizer.setObjectiveFunction(objectiveFunction);

        // Set the initial guess
        optimizer.setStartPoint(new double[]{0.0});

        // Set the goal type to maximize
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Optimize the objective function
        PointValuePair result = optimizer.optimize();

        // Verify that the optimal point is close to the maximum of the objective function
        assertEquals(5.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize_customObjectiveFunction() {
        // Create an instance of NonLinearConjugateGradientOptimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );

        // Define the objective function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return 2 * Math.pow(x, 3) - 4 * Math.pow(x, 2) + 3 * x - 1;
            }
        };

        // Set the objective function
        optimizer.setObjectiveFunction(objectiveFunction);

        // Set the initial guess
        optimizer.setStartPoint(new double[]{0.0});

        // Set the goal type to minimize
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Optimize the objective function
        PointValuePair result = optimizer.optimize();

        // Verify that the optimal point is close to the minimum of the objective function
        assertEquals(1.433, result.getPoint()[0], 0.001);
        assertEquals(-2.045, result.getValue(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimize_unableToBracketOptimum() {
        // Create an instance of NonLinearConjugateGradientOptimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );

        // Define the objective function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };

        // Set the objective function
        optimizer.setObjectiveFunction(objectiveFunction);

        // Set the initial guess
        optimizer.setStartPoint(new double[]{0.0});

        // Optimize the objective function
        optimizer.optimize(); // This should throw a MathIllegalStateException
    }

}