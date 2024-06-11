package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testMinimization() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public Preconditioner getPreconditioner() {
                return new IdentityPreconditioner();
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new SimpleValueChecker(1e-6);
            }
        };
        optimizer.minimize(f, 0.0);
        double expectedX = Precision.round(0.5, 4);
        double expectedF = Precision.round(-1.0, 4);
        assertEquals(expectedX, optimizer.getPoint()[0], 1e-6);
        assertEquals(expectedF, optimizer.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testException1() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.minimize(f, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testException2() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.minimize(f, null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testException3() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public Preconditioner getPreconditioner() {
                throw new RuntimeException("Preconditioner should not throw an exception");
            }
        };
        optimizer.minimize(f, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testException4() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                throw new RuntimeException("Convergence checker should not throw an exception");
            }
        };
        optimizer.minimize(f, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testException5() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public UnivariateSolver getLineSearchSolver() {
                throw new RuntimeException("Line search solver should not throw an exception");
            }
        };
        optimizer.minimize(f, 0.0);
    }

    @Test
    public void testMaxEvaluations() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public Preconditioner getPreconditioner() {
                return new IdentityPreconditioner();
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new SimpleValueChecker(1e-6);
            }

            @Override
            public UnivariateSolver getLineSearchSolver() {
                return new BrentSolver();
            }

            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };
        optimizer.minimize(f, 0.0);
        double expectedX = Precision.round(0.5, 4);
        double expectedF = Precision.round(-1.0, 4);
        assertEquals(expectedX, optimizer.getPoint()[0], 1e-6);
        assertEquals(expectedF, optimizer.getValue(), 1e-6);
        assertTrue(optimizer.getIterations() <= 10);
    }

    @Test
    public void testPreconditioner() {
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            public Preconditioner getPreconditioner() {
                return new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double[] p = variables.clone();
                        for (int i = 0; i < p.length; i++) {
                            p[i] += FastMath.sqrt(Math.abs(r[i])) * 0.1;
                        }
                        return p;
                    }
                };
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new SimpleValueChecker(1e-6);
            }

            @Override
            public UnivariateSolver getLineSearchSolver() {
                return new BrentSolver();
            }
        };
        optimizer.minimize(f, 0.0);
        double expectedX = Precision.round(0.5, 4);
        double expectedF = Precision.round(-1.0, 4);
        assertEquals(expectedX, optimizer.getPoint()[0], 1e-6);
        assertEquals(expectedF, optimizer.getValue(), 1e-6);
    }

    private static class QuadraticFunction implements UnivariateFunction {
        private final double a;
        private final double b;
        private final double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }
}