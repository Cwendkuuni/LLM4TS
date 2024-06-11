import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.QuinticFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void optimize_QuinticFunction_ExpectMinimum() {
        // Given
        UnivariateFunction function = new QuinticFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1e-4);
        double expectedX = 0.0;
        double expectedY = function.value(expectedX);

        // When
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE,
                new double[] { -1.0 }, new double[] { 1.0 });

        // Then
        assertEquals(expectedX, result.getPoint()[0], EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    @Test
    public void optimize_InvalidInterval_ThrowsMathIllegalStateException() {
        // Given
        UnivariateFunction function = new QuinticFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1e-4);

        // When
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, new double[] { 1.0 }, new double[] { -1.0 });
        } catch (MathIllegalStateException e) {
            // Then
            assertEquals("Unable to bracket optimum in line search", e.getMessage());
        }
    }

    @Test
    public void optimize_MaxEvaluationsReached_ReturnsApproximateSolution() {
        // Given
        UnivariateFunction function = new QuinticFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES,
                new BrentSolver(), new NonLinearConjugateGradientOptimizer.IdentityPreconditioner());
        optimizer.setInitialStep(1e-4);
        optimizer.setMaxEvaluations(10); // Set max evaluations to a very low number
        double expectedX = 0.0;
        double expectedY = function.value(expectedX);

        // When
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE,
                new double[] { -1.0 }, new double[] { 1.0 });

        // Then
        assertEquals(expectedX, result.getPoint()[0], EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    @Test
    public void optimize_PolakRibiereFormula_ExpectMinimum() {
        // Given
        UnivariateFunction function = new QuinticFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(1e-4);
        double expectedX = 0.0;
        double expectedY = function.value(expectedX);

        // When
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE,
                new double[] { -1.0 }, new double[] { 1.0 });

        // Then
        assertEquals(expectedX, result.getPoint()[0], EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    @Test
    public void optimize_QuadraticFunction_ExpectMaximum() {
        // Given
        UnivariateFunction function = new QuadraticFunction(-2, 1, 4);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1e-4);
        double expectedX = 1.0;
        double expectedY = function.value(expectedX);

        // When
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE,
                new double[] { -10.0 }, new double[] { 10.0 });

        // Then
        assertEquals(expectedX, result.getPoint()[0], EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    @Test
    public void optimize_ConvergenceCheckerThresholdReached_ExpectMinimum() {
        // Given
        UnivariateFunction function = new QuinticFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1e-4);
        optimizer.setConvergenceChecker(new CustomConvergenceChecker(0.1));
        double expectedX = 0.0;
        double expectedY = function.value(expectedX);

        // When
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE,
                new double[] { -1.0 }, new double[] { 1.0 });

        // Then
        assertEquals(expectedX, result.getPoint()[0], EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    private static class CustomConvergenceChecker implements
            org.apache.commons.math3.optimization.ConvergenceChecker<PointValuePair> {

        private double threshold;

        public CustomConvergenceChecker(double threshold) {
            this.threshold = threshold;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double diff = Math.abs(current.getValue() - previous.getValue());
            return diff < threshold;
        }

    }
}