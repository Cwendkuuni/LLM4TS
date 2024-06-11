
package org.apache.commons.math3.optimization.general;

// Test cases for the NonLinearConjugateGradientOptimizer class

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFunction;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.simple.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.simple.LineSearchSolver;
import org.apache.commons.math3.optim.nonlinear.simple.noderiv.BrentSolver;
import org.apache.commons.math3.special.E;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for NonLinearConjugateGradientOptimizer class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test function for the optimizer. */
    private static final UnivariateFunction TEST_FUNCTION = new ConjugateGradientFunction();

    /** Optimizer under test. */
    private UnivariateOptimizer optimizer;

    /**
     * Set up the optimizer instance for each test.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
    }

    /**
     * Test optimize method with a function having a unique minimum.
     */
    @Test
    public void testOptimizeUniqueMinimum() {
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, 0.0, 1.0, GoalType.MINIMIZE);
        double[] expected = new double[]{FunctionUtils. roots(TEST_FUNCTION)[0], TEST_FUNCTION.value(FunctionUtils. roots(TEST_FUNCTION)[0])};
        assertEquals(expected[0], result.getPoint()[0], 1e-8);
        assertEquals(expected[1], result.getValue(), 1e-8);
    }

    /**
     * Test optimize method with a function having multiple minima.
     */
    @Test
    public void testOptimizeMultipleMinima() {
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, 0.2, 1.5, GoalType.MINIMIZE);
        double[] expected = new double[]{FunctionUtils. roots(TEST_FUNCTION)[1], TEST_FUNCTION.value(FunctionUtils. roots(TEST_FUNCTION)[1])};
        assertEquals(expected[0], result.getPoint()[0], 1e-8);
        assertEquals(expected[1], result.getValue(), 1e-8);
    }

    /**
     * Test optimize method when the function has no minimum.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNoMinimum() {
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return E.E;
            }
        }, -1.0, 1.0, GoalType.MINIMIZE);
    }

    /**
     * Test setInitialStep method with a positive value.
     */
    @Test
    public void testSetInitialStepPositive() {
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 1e-8);
    }

    /**
     * Test setInitialStep method with a non-positive value.
     */
    @Test
    public void testSetInitialStepNonPositive() {
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 1e-8);
    }
}
