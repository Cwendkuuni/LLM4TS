
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize_Minimize() {
        // Create the optimizer with default parameters
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);

        // Define the objective function
        UnivariateFunction objectiveFunction = x -> Math.pow(x, 2) - 4 * x + 5;

        // Set the start point
        PointValuePair startPoint = new PointValuePair(new double[]{2.0}, objectiveFunction.value(2.0));

        // Set the goal type
        GoalType goalType = GoalType.MINIMIZE;

        // Set the maximum number of evaluations
        int maxEvaluations = 1000;

        // Optimize the function
        PointValuePair result = optimizer.optimize(maxEvaluations, objectiveFunction, goalType, startPoint);

        // Verify the result
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-5);
        Assert.assertEquals(1.0, result.getValue(), 1e-5);
    }

    @Test
    public void testOptimize_Maximize() {
        // Create the optimizer with default parameters
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);

        // Define the objective function
        UnivariateFunction objectiveFunction = x -> Math.pow(x, 2) - 4 * x + 5;

        // Set the start point
        PointValuePair startPoint = new PointValuePair(new double[]{2.0}, objectiveFunction.value(2.0));

        // Set the goal type
        GoalType goalType = GoalType.MAXIMIZE;

        // Set the maximum number of evaluations
        int maxEvaluations = 1000;

        // Optimize the function
        PointValuePair result = optimizer.optimize(maxEvaluations, objectiveFunction, goalType, startPoint);

        // Verify the result
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-5);
        Assert.assertEquals(9.0, result.getValue(), 1e-5);
    }
}

