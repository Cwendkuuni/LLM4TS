package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.general.LinearConstraint;
import org.apache.commons.math3.optimization.general.QuadraticConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.QuadraticConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.linear.IdentityPreconditioner;
import org.apache.commons.math3.optimization.nonlinear.scalar.linear.LineSearchFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.linear.LineSearchSolver;
import org.apache.commons.math3.optimization.nonlinear.scalar.linear.SimpleValueChecker;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        assertNotNull(optimizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidUpdateFormula() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null, checker, lineSearchSolver, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullChecker() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, null, lineSearchSolver, preconditioner);
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(3.0);
        assertEquals(3.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] gradient(double x) {
                return new double[] {2.0 * x};
            }
        };
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(0.0, 0.0), null);
        assertEquals(0.0, result.getPoint()[0], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithLineSearch() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2.0 * x + 1.0;
            }

            @Override
            public double[] gradient(double x) {
                return new double[] {3.0 * Math.pow(x, 2) + 2.0, 6.0 * x + 2.0};
            }
        };
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new LineSearchSolver(new SimpleValueChecker(), new GoldenSectionFinder(), new BrentSolver());
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(1.0, 2.0), null);
        assertEquals(1.0, result.getPoint()[0], 0.001);
        assertEquals(-1.547005887163661, result.getValue(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithInvalidUpdateFormula() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null, checker, lineSearchSolver, preconditioner);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] gradient(double x) {
                return new double[] {2.0 * x};
            }
        }, new PointValuePair(0.0, 0.0), null);
    }

    @Test
    public void testFindUpperBound() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1.0 + Math.pow(x, 2));
            }
        };
        double a = 1.0;
        double h = 1.0;
        double b = NonLinearConjugateGradientOptimizer.this.findUpperBound(function, a, h);
        assertEquals(1.945939155364754, b, 0.0001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundInvalidFunction() {
        double a = 1.0;
        double h = 1.0;
        NonLinearConjugateGradientOptimizer.this.findUpperBound(null, a, h);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundInvalidA() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1.0 + Math.pow(x, 2));
            }

            @Override
            public double[] gradient(double x) {
                return new double[0];
            }
        };
        double a = 0.0;
        double h = 1.0;
        NonLinearConjugateGradientOptimizer.this.findUpperBound(function, a, h);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundInvalidH() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1.0 + Math.pow(x, 2));
            }

            @Override
            public double[] gradient(double x) {
                return new double[0];
            }
        };
        double a = 1.0;
        double h = 0.0;
        NonLinearConjugateGradientOptimizer.this.findUpperBound(function, a, h);
    }

    @Test
    public void testIdentityPreconditioner() {
        double[] variables = new double[] {1.0, 2.0};
        double[] r = new double[] {3.0, 4.0};
        double[] result = NonLinearConjugateGradientOptimizer.this.preconditioner.precondition(variables, r);
        assertArrayEquals(r, result);
    }

    @Test
    public void testLineSearchFunction() {
        double[] x = new double[] {1.0, 2.0};
        UnivariateFunction function = new LineSearchFunction(x);
        double result = function.value(1.0);
        assertEquals(-3.0, result, 0.001);
    }
}

class LineSearchFunction implements UnivariateFunction {
    private final double[] searchDirection;

    public LineSearchFunction(final double[] searchDirection) {
        this.searchDirection = searchDirection;
    }

    @Override
    public double value(double x) {
        double[] shiftedPoint = new double[searchDirection.length];
        System.arraycopy(xPoint, 0, shiftedPoint, 0, searchDirection.length);
        for (int i = 0; i < shiftedPoint.length; ++i) {
            shiftedPoint[i] += x * searchDirection[i];
        }

        double[] gradient = NonLinearConjugateGradientOptimizer.this.computeObjectiveGradient(shiftedPoint);

        double dotProduct = 0;
        for (int i = 0; i < gradient.length; ++i) {
            dotProduct += gradient[i] * searchDirection[i];
        }

        return dotProduct;
    }
}