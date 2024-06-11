import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.LinearSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.BrentSolver;
import org.apache.commons.math3.optim.initializers.ArrayInitializer;
import org.apache.commons.math3.optim.initializers.ArrayInitializer.DenseInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}.
     */
    @Test
    public void testOptimizerWithCustomParameters() {
        final UnivariateFunction function = new QuadraticFunction(-1);
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        final SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        final BrentSolver solver = new BrentSolver();
        final IdentityPreconditioner preconditioner = new IdentityPreconditioner();

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, solver, preconditioner);

        final double[] startPoint = new double[]{0.5d, 0.5d};
        final PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(0.0d, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{0.0d, 0.0d}, result.getPoint(), 1e-8);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}.
     */
    @Test
    public void testOptimizerWithDefaultParameters() {
        final UnivariateFunction function = new QuadraticFunction(-1);
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula);

        final double[] startPoint = new double[]{0.5d, 0.5d};
        final PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(0.0d, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{0.0d, 0.0d}, result.getPoint(), 1e-8);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with custom line search solver and goal type.
     */
    @Test
    public void testOptimizerWithCustomSolverAndGoalType() {
        final UnivariateFunction function = new QuadraticFunction(-1);
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        final CustomBrentSolver solver = new CustomBrentSolver();
        final GoalType goalType = GoalType.MINIMIZE;

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, solver, new IdentityPreconditioner(), goalType);

        final double[] startPoint = new double[]{0.5d, 0.5d};
        final PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(0.0d, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{0.0d, 0.0d}, result.getPoint(), 1e-8);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @Test
    public void testSetInitialStep() {
        final UnivariateFunction function = new QuadraticFunction(-1);
        final SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        final BrentSolver solver = new BrentSolver();
        final IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, preconditioner);

        optimizer.setInitialStep(2.0);
        final double[] startPoint = new double[]{0.5d, 0.5d};
        final PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(0.0d, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{0.0d, 0.0d}, result.getPoint(), 1e-8);
    }

    /**
     * Quadratic function for testing.
     */
    private static class QuadraticFunction implements UnivariateFunction {

        private final double coefficient;

        /**
         * Constructor.
         * @param coefficient the quadratic coefficient.
         */
        public QuadraticFunction(final double coefficient) {
            this.coefficient = coefficient;
        }

        /** {@inheritDoc} */
        public double value(double x) {
            double value = 0;
            for (int i = 0; i < x.length; ++i) {
                value += x[i] * x[i] * coefficient;
            }
            return value;
        }
    }

    /**
     * Custom Brent solver for testing.
     */
    private static class CustomBrentSolver extends BrentSolver {

        /** {@inheritDoc} */
        @Override
        protected double bracketingBrent(UnivariateFunction f, double a, double b) {
            double brent = super.bracketingBrent(f, a, b);
            if (brent >= b) {
                brent = a + ((b - a) / 2);
            }
            return brent;
        }
    }
}