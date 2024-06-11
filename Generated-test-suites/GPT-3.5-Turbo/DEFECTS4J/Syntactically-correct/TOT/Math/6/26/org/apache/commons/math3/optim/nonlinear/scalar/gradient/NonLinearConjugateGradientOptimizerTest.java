import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.functions.QuadraticFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize_quadraticFunction_minimize() {
        // Create a quadratic function: f(x) = x^2 - 4x + 4
        UnivariateFunction function = new QuadraticFunction(1, -4, 4);
        
        // Create the optimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, 
                new SimpleValueChecker());
        
        // Setup the optimization problem
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMaxEvaluations(100);
        
        // Optimize the function
        PointValuePair result = optimizer.optimize(function, 
                GoalType.MINIMIZE, 
                new double[] {-10}, // initial point
                new double[] {10}); // search interval
        
        // Check the result
        double[] expectedPoint = {2.0};
        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-5);
    }

    @Test
    public void testOptimize_quadraticFunction_maximize() {
        // Create a quadratic function: f(x) = -(x^2 - 4x + 4)
        UnivariateFunction function = new QuadraticFunction(1, -4, 4).negate();
        
        // Create the optimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, 
                new SimpleValueChecker());
        
        // Setup the optimization problem
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMaxEvaluations(100);
        
        // Optimize the function
        PointValuePair result = optimizer.optimize(function, 
                GoalType.MAXIMIZE, 
                new double[] {-10}, // initial point
                new double[] {10}); // search interval
        
        // Check the result
        double[] expectedPoint = {2.0};
        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-5);
    }
    
    @Test
    public void testOptimize_quadraticFunction_error() {
        // Create a quadratic function: f(x) = x^2 - 4x + 4
        UnivariateFunction function = new QuadraticFunction(1, -4, 4);
        
        // Create the optimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, 
                new SimpleValueChecker());
        
        // Setup the optimization problem
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Try to optimize with invalid initial point and search interval
        double[] initialPoint = {Double.NaN};
        double[] searchInterval = {2.0, 1.0};
        
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, initialPoint, searchInterval);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid search interval"));
        }
    }

}