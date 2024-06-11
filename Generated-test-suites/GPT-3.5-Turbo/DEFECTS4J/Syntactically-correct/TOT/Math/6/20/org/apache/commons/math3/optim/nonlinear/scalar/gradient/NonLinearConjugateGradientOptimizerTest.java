import org.junit.Test;
import org.apache.commons.math3.optimization.PointValuePair;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Initialize optimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setMaxEvaluations(1000);

        // Test Objective Function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                // quadratic function: f(x) = x^2
                return Math.pow(x, 2);
            }
        };

        PointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 0.0);
        assertEquals(0.0, result.getPoint()[0], 1e-15);

        result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 5.0);
        assertEquals(0.0, result.getPoint()[0], 1e-15);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver(),
                new IdentityPreconditioner());

        LineSearchFunction searchFunction = optimizer.new LineSearchFunction(new double[] {1});
        
        double upperBound = optimizer.findUpperBound(searchFunction, 1.0, 0.5);
        assertEquals(2.0, upperBound, 1e-15);
        
        upperBound = optimizer.findUpperBound(searchFunction, 0.0, 0.1);
        assertEquals(0.64, upperBound, 1e-15);
        
    }

}