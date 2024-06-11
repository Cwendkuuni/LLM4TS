package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        UnivariateFunction f = new QuadraticFunction(-1, 4);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(1.0);

        PointValuePair result = optimizer.optimize(f, 0, null);

        assertEquals(0.0, result.getPoint()[0], 0.001);
        assertEquals(-2.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization of a simple function with a local minimum and a local maximum.
     */
    @Test
    public void testFunctionWithLocalMinimumAndLocalMaximum() {
        UnivariateFunction f = new TestFunctionWithLocalMinimumAndLocalMaximum();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(0.1);

        PointValuePair result = optimizer.optimize(f, 0, null);

        assertEquals(-1.0, result.getPoint()[0], 0.001);
        assertEquals(1.5, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a non-converging function.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNonConvergingFunction() {
        UnivariateFunction f = new NonConvergingFunction();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(1.0);

        optimizer.optimize(f, 0, null);
    }

    /**
     * Test optimization with preconditioning.
     */
    @Test
    public void testPreconditioning() {
        UnivariateFunction f = new QuadraticFunction(-1, 4);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(1.0);

        // Define a custom preconditioner
        class CustomPreconditioner implements Preconditioner {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                for (int i = 0; i < variables.length; ++i) {
                    variables[i] += 1.0;
                    r[i] *= 2.0;
                }
                return r;
            }
        }

        Preconditioner customPreconditioner = new CustomPreconditioner();
        optimizer.setPreconditioner(customPreconditioner);

        PointValuePair result = optimizer.optimize(f, 0, null);

        assertEquals(1.0, result.getPoint()[0], 0.001);
        assertEquals(-4.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a large number of evaluations.
     */
    @Test
    public void testLargeNumberOfEvaluations() {
        UnivariateFunction f = new QuadraticFunction(-1, 4);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(10000);

        PointValuePair result = optimizer.optimize(f, 0, null);

        assertEquals(0.0, result.getPoint()[0], 0.001);
        assertEquals(-2.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with different update formulas.
     */
    @Test
    public void testDifferentUpdateFormulas() {
        UnivariateFunction f = new QuadraticFunction(-1, 4);
        NonLinearConjugateGradientOptimizer optimizerFLReeves = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizerFLReeves.setInitialStep(1.0);

        PointValuePair resultFLReeves = optimizerFLReeves.optimize(f, 0, null);

        NonLinearConjugateGradientOptimizer optimizerPR = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizerPR.setInitialStep(1.0);

        PointValuePair resultPR = optimizerPR.optimize(f, 0, null);

        assertEquals(0.0, resultFLReeves.getPoint()[0], 0.001);
        assertEquals(-2.0, resultFLReeves.getValue(), 0.001);

        assertEquals(0.0, resultPR.getPoint()[0], 0.001);
        assertEquals(-2.0, resultPR.getValue(), 0.001);
    }
}

class QuadraticFunction implements UnivariateFunction {

    private final double a;
    private final double b;

    public QuadraticFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double value(double x) {
        return a * Math.pow(x, 2) + b;
    }
}

class TestFunctionWithLocalMinimumAndLocalMaximum implements UnivariateFunction {

    @Override
    public double value(double x) {
        if (x < -2) {
            return Math.pow(x + 2, 2) - 1;
        } else if (x > 1) {
            return -Math.pow(x - 1, 2) + 1;
        } else {
            return 0;
        }
    }
}

class NonConvergingFunction implements UnivariateFunction {

    private int count = 0;

    @Override
    public double value(double x) {
        count++;

        if (count % 3 == 0) {
            return Math.sin(x) / x;
        } else {
            return -Math.sin(x) / x;
        }
    }
}