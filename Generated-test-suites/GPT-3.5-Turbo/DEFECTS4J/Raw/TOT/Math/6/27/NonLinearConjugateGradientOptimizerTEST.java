import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.DirectSearchOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize_minimizeGoalType() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker);

        // Test with an objective function that decreases to a minimum
        // along the line defined by the search direction
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        PointValuePair optimum = optimizer.optimize(objective, GoalType.MINIMIZE,
                                                    new double[] {1.0},
                                                    new double[] {0.5});
        double[] actualPoint = optimum.getPoint();
        double actualValue = optimum.getValue();

        assertEquals(0.0, actualValue, 1e-10);  // The minimum value should be 0
        assertEquals(0.0, actualPoint[0], 1e-10);  // The optimal point should be (0, 0)
    }

    @Test
    public void testDoOptimize_maximizeGoalType() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker);

        // Test with an objective function that increases to a maximum
        // along the line defined by the search direction
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return -x * x;
            }
        };

        PointValuePair optimum = optimizer.optimize(objective, GoalType.MAXIMIZE,
                                                    new double[] {1.0},
                                                    new double[] {0.5});
        double[] actualPoint = optimum.getPoint();
        double actualValue = optimum.getValue();

        assertEquals(0.0, actualValue, 1e-10);  // The maximum value should be 0
        assertEquals(0.0, actualPoint[0], 1e-10);  // The optimal point should be (0, 0)
    }

    @Test
    public void testDoOptimize_withPreconditioner() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker);

        // Test with a preconditioner to improve convergence
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        // Define a preconditioner that scales the gradient by 0.5
        Preconditioner preconditioner = new Preconditioner() {
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[r.length];
                for (int i = 0; i < r.length; i++) {
                    result[i] = r[i] * 0.5;
                }
                return result;
            }
        };

        optimizer.setPreconditioner(preconditioner);

        PointValuePair optimum = optimizer.optimize(objective, GoalType.MINIMIZE,
                                                    new double[] {1.0},
                                                    new double[] {0.5});
        double[] actualPoint = optimum.getPoint();
        double actualValue = optimum.getValue();

        assertEquals(0.0, actualValue, 1e-10);  // The minimum value should be 0
        assertEquals(0.0, actualPoint[0], 1e-10);  // The optimal point should be (0, 0)
    }
}