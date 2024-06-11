package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.Preconditioner;
import org.apache.commons.math3.optimization.nonlinear.scalardifferential.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LBracket;
import org.apache.commons.math3.analysis.solvers.SimpleBracket;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.Constants;
import org.apache.commons.math3.analysis.function.Multiply;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.analysis.function.RationalFunction;

import java.util.Arrays;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Tests the optimizer with a simple test function.
     */
    @org.junit.Test
    public void testSimple() {
        final UnivariateFunction f = new Add(new Constants(1), new Multiply(new Pow(new Multiply(x, x), 2), 5));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        PointValuePair result = optimizer.optimize(f, 0.0, 10.0);

        assertEquals("Optimum not found", 2.0, result.getPoint()[0], 1e-6);
        assertEquals("Optimum value not correct", 4.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a test function with multiple roots.
     */
    @org.junit.Test
    public void testMultipleRoots() {
        final UnivariateFunction f = new Add(new Multiply(new RationalFunction(1, new Rational(2, 3)), x),
                                             new Multiply(new RationalFunction(1, new Rational(3, 2)), x));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        PointValuePair result1 = optimizer.optimize(f, -5.0, 5.0);
        PointValuePair result2 = optimizer.optimize(f, -5.0, 5.0);

        assertEquals("Wrong number of roots found", 2, optimizer.getNumIterations());
        assertEquals("Roots not found", 0.0, result1.getPoint()[0], 1e-6);
        assertEquals("Roots not found", 2.5, result2.getPoint()[0], 1e-6);
        assertEquals("Root values not correct", 0.0, result1.getValue(), 1e-6);
        assertEquals("Root values not correct", 3.75, result2.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a test function with no roots.
     */
    @org.junit.Test
    public void testNoRoots() {
        final UnivariateFunction f = new Add(new Multiply(new Pow(x, 2), 2), 1);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        try {
            optimizer.optimize(f, -10.0, 10.0);
            fail("Optimization should not succeed");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Tests the optimizer with a test function where the conjugate gradient
     * method fails to converge.
     */
    @org.junit.Test
    public void testDivergence() {
        final UnivariateFunction f = new RationalFunction(-1, new Rational(x, 2));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        try {
            optimizer.optimize(f, -10.0, 10.0);
            fail("Optimization should not succeed");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Tests the optimizer with a test function where the line search fails to
     * converge.
     */
    @org.junit.Test
    public void testLineSearchDivergence() {
        final UnivariateFunction f = new RationalFunction(-1, new Rational(x, 2));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new LBracket(new SimpleBracket(-10.0, 10.0), 1e-6));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        try {
            optimizer.optimize(f, -10.0, 10.0);
            fail("Optimization should not succeed");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Tests the optimizer with a test function where the line search finds a
     * root, but the conjugate gradient method does not converge.
     */
    @org.junit.Test
    public void testNoConvergence() {
        final UnivariateFunction f = new Add(new Multiply(x, x), Math.sin(x));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        try {
            optimizer.optimize(f, -10.0, 10.0);
            fail("Optimization should not succeed");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Tests the optimizer with a test function where the line search and the
     * conjugate gradient method both converge.
     */
    @org.junit.Test
    public void testConvergence() {
        final UnivariateFunction f = new Multiply(x, Math.exp(-10 * x));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                         new SimpleValueChecker(1e-8),
                                                                                                         new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        PointValuePair result = optimizer.optimize(f, -10.0, 10.0);

        assertEquals("Optimum not found", 1.0, result.getPoint()[0], 1e-6);
        assertEquals("Optimum value not correct", 0.0, result.getValue(), 1e-6);
    }
}