package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearUnivariateSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioners.IdentityPreconditioner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setInitialStep(1.5);
        optimizer.setMaxEvaluations(1000);
    }

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final UnivariateFunction f = new QuadraticFunction(-2);
        final double[] startPoint = {1.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final NonlinearPointValuePair result = optimizer.optimize(f, startPoint, checker);
        assertEquals(-1.0, result.getPoint()[0], eps);
        assertEquals(-2.0, result.getValue(), eps);
    }

    /**
     * Test optimization of a simple quadratic function with constraints.
     */
    @Test
    public void testQuadraticFunctionWithConstraints() {
        final UnivariateFunction f = new QuadraticFunction(-2);
        final double[] startPoint = {1.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0});
        final LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, new double[]{0.0}, 1.0);
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                      checker,
                                                                                      new BrentSolver()) {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setInitialStep(1.5);
        optimizer.setMaxEvaluations(1000);
        optimizer.setConstraints(new LinearConstraint[]{constraint});

        final NonlinearPointValuePair result = optimizer.optimize(new LinearObjectiveFunction[]{objective},
                                                                 startPoint);
        assertEquals(-1.0, result.getPoint()[0], eps);
        assertEquals(-2.0, result.getValue(), eps);
    }

    /**
     * Test optimization of a non-quadratic function.
     */
    @Test
    public void testNonQuadraticFunction() {
        final UnivariateFunction f = x -> Math.sin(x) * Math.sin(x);
        final double[] startPoint = {0.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final NonlinearPointValuePair result = optimizer.optimize(f, startPoint, checker);
        assertEquals(Math.PI / 2.0, result.getPoint()[0], eps);
        assertEquals(0.0, result.getValue(), eps);
    }

    /**
     * Test optimization of a non-quadratic function with constraints.
     */
    @Test
    public void testNonQuadraticFunctionWithConstraints() {
        final UnivariateFunction f = x -> Math.sin(x) * Math.sin(x);
        final double[] startPoint = {0.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{0.0});
        final LinearConstraint constraint = new LinearConstraint(new double[]{-1.0}, new double[]{0.0}, 1.0);
        final UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                      checker,
                                                                                      new BrentSolver()) {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setInitialStep(1.5);
        optimizer.setMaxEvaluations(1000);
        optimizer.setConstraints(new LinearConstraint[]{constraint});

        final NonlinearPointValuePair result = optimizer.optimize(new LinearObjectiveFunction[]{objective},
                                                                 startPoint);
        assertEquals(Math.PI / 2.0, result.getPoint()[0], eps);
        assertEquals(0.0, result.getValue(), eps);
    }

    /**
     * Test optimization with preconditioner.
     */
    @Test
    public void testOptimizationWithPreconditioner() {
        final UnivariateFunction f = x -> x * x;
        final double[] startPoint = {1.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final IdentityPreconditioner preconditioner = new IdentityPreconditioner();

        final NonlinearPointValuePair result = optimizer.optimize(f, startPoint, checker, preconditioner);
        assertEquals(0.0, result.getPoint()[0], eps);
        assertEquals(0.0, result.getValue(), eps);
    }

    /**
     * Test optimization with null preconditioner.
     */
    @Test
    public void testOptimizationWithNullPreconditioner() {
        final UnivariateFunction f = x -> x * x;
        final double[] startPoint = {1.0};
        final double eps = 1e-6;
        final SimpleValueChecker checker = new SimpleValueChecker(eps);

        final NonlinearPointValuePair result = optimizer.optimize(f, startPoint, checker, null);
        assertEquals(0.0, result.getPoint()[0], eps);
        assertEquals(0.0, result.getValue(), eps);
    }

    /**
     * Test optimization with non-conjugate update formula.
     */
    @Test
    public void testOptimizationWithNonConjugateUpdateFormula() {
        assertThrows(IllegalArgumentException.class, () -> new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE));
    }

    /**
     * Test optimization with non-supported line search solver.
     */
    @Test
    public void testOptimizationWithNonSupportedLineSearchSolver() {
        assertThrows(IllegalArgumentException.class, () -> new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                    new MyBrentSolver()));
    }

    /**
     * Test optimization with non-supported preconditioner.
     */
    @Test
    public void testOptimizationWithNonSupportedPreconditioner() {
        assertThrows(IllegalArgumentException.class, () -> new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                    new SimpleValueChecker(),
                                                                                                    new MyPreconditioner()));
    }

    /**
     * Test optimization with illegal initial step.
     */
    @Test
    public void testOptimizationWithIllegalInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setInitialStep(-1.0);

        final UnivariateFunction f = x -> x * x;
        final double[] startPoint = {1.0};
        final SimpleValueChecker checker = new SimpleValueChecker();

        assertThrows(MathIllegalStateException.class, () -> optimizer.optimize(f, startPoint, checker));
    }

    private static class MyBrentSolver extends BrentSolver {
        @Override
        public double solve(int maxEval, UnivariateFunction f, double x1, double x2, double tol) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MyPreconditioner implements Preconditioner {
        @Override
        public double[] precondition(double[] variables, double[] r) {
            // This is just a placeholder. In practice, the preconditioner
            // should be implemented according to the specific problem.
            return Arrays.stream(r).map(x -> x * 2).toArray();
        }
    }

    /**
     * A quadratic function for testing.
     */
    private static class QuadraticFunction implements UnivariateFunction {
        private final double a;

        public QuadraticFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return a * x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * a * x;
        }
    }
}