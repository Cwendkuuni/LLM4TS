import org.apache.commons.math3.analysis.QuinticFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.IdentityPreconditioner;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.Preconditioner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        // Create an optimizer with FLETCHER_REEVES update formula
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        // Create a quadratic function with a known minimum at x = 2
        QuinticFunction function = new QuinticFunction();

        // Set the start point for the optimization
        double[] startPoint = {1.0};
        optimizer.setStartPoint(startPoint);

        // Set the convergence checker
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getPoint()[0] - 2.0) < 1e-6;
            }
        };
        optimizer.setConvergenceChecker(checker);

        // Optimize the function
        PointValuePair result = optimizer.optimize(function, NonLinearConjugateGradientOptimizer.GoalType.MINIMIZE);

        // Check that the result is close to the known minimum
        assertEquals(2.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_withPreconditioner() {
        // Create an optimizer with POLAK_RIBIERE update formula and a custom preconditioner
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setPreconditioner(new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                // Multiply the gradient by a factor of 2
                double[] result = new double[r.length];
                for (int i = 0; i < r.length; i++) {
                    result[i] = 2 * r[i];
                }
                return result;
            }
        });

        // Create a quadratic function with a known minimum at x = 2
        QuinticFunction function = new QuinticFunction();

        // Set the start point for the optimization
        double[] startPoint = {1.0};
        optimizer.setStartPoint(startPoint);

        // Set the convergence checker
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getPoint()[0] - 2.0) < 1e-6;
            }
        };
        optimizer.setConvergenceChecker(checker);

        // Optimize the function
        PointValuePair result = optimizer.optimize(function, NonLinearConjugateGradientOptimizer.GoalType.MINIMIZE);

        // Check that the result is close to the known minimum
        assertEquals(2.0, result.getPoint()[0], 1e-6);
    }

}