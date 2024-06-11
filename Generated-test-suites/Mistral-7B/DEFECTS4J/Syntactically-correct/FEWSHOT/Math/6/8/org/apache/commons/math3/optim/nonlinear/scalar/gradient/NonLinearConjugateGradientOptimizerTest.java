package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.BrentSolver;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.optimize(f, 0.0, 1.0);

        PointValuePair result = optimizer.getCurrentPoint();
        double expected = Math.PI / 2;
        double tolerance = 1e-6;

        assertEquals(expected, result.getPoint()[0], tolerance);
        assertEquals(0.0, result.getValue(), tolerance);
    }

    @Test
    public void testPreconditioner() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double factor = 2.0;
                        for (int i = 0; i < variables.length; i++) {
                            r[i] *= factor;
                        }
                        return r;
                    }
                });

        optimizer.optimize(f, 0.0, 1.0);

        PointValuePair result = optimizer.getCurrentPoint();
        double expected = Math.PI / 2;
        double tolerance = 1e-6;

        assertEquals(expected, result.getPoint()[0], tolerance);
        assertEquals(0.0, result.getValue(), tolerance);

        double[] r = optimizer.getGradient();
        double factor = 0.5;
        for (int i = 0; i < r.length; i++) {
            r[i] *= factor;
        }

        optimizer.precondition(r);

        assertEquals(-Math.cos(expected), r[0], tolerance);
        assertEquals(-2.0 * Math.cos(expected) * Math.sin(expected), r[1], tolerance);
    }
}