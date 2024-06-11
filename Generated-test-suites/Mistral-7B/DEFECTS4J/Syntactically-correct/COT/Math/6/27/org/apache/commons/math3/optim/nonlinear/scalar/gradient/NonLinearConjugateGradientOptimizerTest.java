package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.LineSearchFunction;
import org.apache.commons.math3.optimization.UnivariateFunction;

/**
 * JUnit test cases for NonLinearConjugateGradientOptimizer class.
 */
@Deprecated
public class NonLinearConjugateGradientOptimizerTest {

    

    @org.junit.Test
    public void testConstructorWithDefaultUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidUpdateFormula() {
        new NonLinearConjugateGradientOptimizer("INVALID_UPDATE_FORMULA");
    }

    @org.junit.Test
    public void testSetterForInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(5.0);
        assertEquals(5.0, optimizer.getInitialStep(), 0.0);
    }

    @org.junit.Test
    public void testDoOptimize_SimpleTest() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        UnivariateFunction function = new FuncTest();
        double[] startingPoint = new double[]{-1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startingPoint);
        assertEquals(-0.666666687274146, result.getPoint()[0], 0.0001);
        assertEquals(-0.111111137146232, result.getValue(), 0.0001);
    }

    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimize_ConvergenceCheckerThrowsException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                throw new MathIllegalStateException();
            }
        }, new BrentSolver());
        UnivariateFunction function = new FuncTest();
        double[] startingPoint = new double[]{-1.0, 1.0};
        optimizer.optimize(function, startingPoint);
    }

    

    /**
     * Test function for optimization.
     */
    private static class FuncTest implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - 1;
        }

        @Override
        public double[] gradient(double[] x) {
            double[] gradient = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                gradient[i] = 2 * Math.pow(x[0] * x[0] + x[1] * x[1], 1) * x[i];
            }
            return gradient;
        }
    }
}