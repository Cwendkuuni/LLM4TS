package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNonLinearConjugateGradientOptimizerInvalidUpdateFormula() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(-1);
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testOptimize() throws MathIllegalStateException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) + 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2.0 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        GoalType goalType = GoalType.MINIMIZE;

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function, 0.0, null);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);

        optimizer.setInitialStep(2.0);
        result = optimizer.optimize(function, 0.0, null);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 2.0}, result.getPoint(), 1e-6);

        Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] p = variables.clone();
                System.arraycopy(r, 0, p, 0, r.length);
                double normR = 0;
                for (int i = 0; i < p.length; i++) {
                    normR += p[i] * p[i];
                }
                for (int i = 0; i < p.length; i++) {
                    p[i] /= Math.sqrt(normR);
                }
                return p;
            }
        };
        optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), customPreconditioner);
        optimizer.setInitialStep(1.0);
        result = optimizer.optimize(function, 0.0, null);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.0 / Math.sqrt(2.0)}, result.getPoint(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeInvalidFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double[] gradient(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(function, 0.0, null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNullStartPoint() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) + 1;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2.0 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(function, null, null);
    }

    @Test
    public void testConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                return previous != null && Math.abs(current.getValue() - previous.getValue()) < 1e-6;
            }
        };

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker, new BrentSolver(), new IdentityPreconditioner());
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2.0 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        optimizer.setMaxEvaluations(100);
        PointValuePair result = optimizer.optimize(function, 1.0, null);
        assertTrue(convergenceChecker.converged(optimizer.getIterations(), result, null));
    }

    @Test
    public void testBrentSolver() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2.0 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                return previous != null && Math.abs(current.getValue() - previous.getValue()) < 1e-6;
            }
        };

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setMaxEvaluations(100);
        PointValuePair result = optimizer.optimize(function, 1.0, null);
        assertTrue(convergenceChecker.converged(optimizer.getIterations(), result, null));
    }

    @Test
    public void testIdentityPreconditioner() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) + 1;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2.0 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                return previous != null && Math.abs(current.getValue() - previous.getValue()) < 1e-6;
            }
        };

        Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] p = variables.clone();
                System.arraycopy(r, 0, p, 0, r.length);
                double normR = 0;
                for (int i = 0; i < p.length; i++) {
                    normR += p[i] * p[i];
                }
                for (int i = 0; i < p.length; i++) {
                    p[i] /= Math.sqrt(normR);
                }
                return p;
            }
        };

        ConjugateGradientOptimizer optimizer1 = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer1.setMaxEvaluations(100);
        PointValuePair result1 = optimizer1.optimize(function, 1.0, null);
        assertTrue(convergenceChecker.converged(optimizer1.getIterations(), result1, null));

        ConjugateGradientOptimizer optimizer2 = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), customPreconditioner);
        optimizer2.setMaxEvaluations(100);
        PointValuePair result2 = optimizer2.optimize(function, 1.0, null);
        assertTrue(convergenceChecker.converged(optimizer2.getIterations(), result2, null));
    }
}