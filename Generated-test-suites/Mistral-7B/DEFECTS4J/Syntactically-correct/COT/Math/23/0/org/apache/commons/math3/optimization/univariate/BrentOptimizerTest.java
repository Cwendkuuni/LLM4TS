package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.test.AbstractUnivariateOptimizationTest;
import org.junit.Test;

/**
 * Test cases for BrentOptimizer class
 */
public class BrentOptimizerTest extends AbstractUnivariateOptimizationTest {

    /**
     * Test constructor with given relative and absolute tolerance and
     * ConvergenceChecker.
     * @throws NumberIsTooSmallException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testBrentOptimizer() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        assertEquals("Relative tolerance is not set to the correct value", 1e-5, optimizer.getRelativeThreshold(), 1e-7);
        assertEquals("Absolute tolerance is not set to the correct value", 1e-5, optimizer.getAbsoluteThreshold(), 1e-7);
        assertNull("ConvergenceChecker should be null", optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with given relative and absolute tolerance.
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooSmallException
     */
    @Test
    public void testBrentOptimizerWithOnlyTolerances() throws NotStrictlyPositiveException, NumberIsTooSmallException {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals("Relative tolerance is not set to the correct value", 1e-5, optimizer.getRelativeThreshold(), 1e-7);
        assertEquals("Absolute tolerance is not set to the correct value", 1e-5, optimizer.getAbsoluteThreshold(), 1e-7);
        assertNull("ConvergenceChecker should be null", optimizer.getConvergenceChecker());
    }

    /**
     * Test doOptimize method with simple bounds and a univariate function.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithSimpleBoundsAndSimpleFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setBounds(new SimpleBounds(0, 5));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 2.5;
        double expectedValue = 6.25;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with initial guess and a univariate function.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithInitialGuessAndSimpleFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setInitialGuess(new InitialGuess(1.0));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 1.712132868;
        double expectedValue = -1.022352547;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with initial guess, simple bounds, and a univariate function.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithInitialGuessBoundsAndSimpleFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setInitialGuess(new InitialGuess(1.0));
        optimizer.setBounds(new SimpleBounds(0, 5));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 1.712132868;
        double expectedValue = -1.022352547;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with initial guess, bounds, and a univariate function
     * which does not have a minimum in the bounds.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeWithInitialGuessBoundsAndNoMinimumFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setInitialGuess(new InitialGuess(1.0));
        optimizer.setBounds(new SimpleBounds(0, 1));
        optimizer.optimize();
    }

    /**
     * Test doOptimize method with relative tolerance set to zero.
     * @throws Exception
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimizeWithZeroRelativeTolerance() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.optimize();
    }

    /**
     * Test doOptimize method with absolute tolerance set to zero.
     * @throws Exception
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimizeWithZeroAbsoluteTolerance() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0, null);
        optimizer.setFunction(function);
        optimizer.optimize();
    }

    /**
     * Test doOptimize method with user-defined convergence checker.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithUserDefinedConvergenceChecker() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1e-6;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 1.712132868;
        double expectedValue = -1.022352547;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with user-defined convergence checker and bounds.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithUserDefinedConvergenceCheckerAndBounds() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1e-6;
            }
        });
        optimizer.setBounds(new SimpleBounds(0, 5));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 1.712132868;
        double expectedValue = -1.022352547;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with user-defined convergence checker, initial guess and bounds.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithUserDefinedConvergenceCheckerInitialGuessAndBounds() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1e-6;
            }
        });
        optimizer.setInitialGuess(new InitialGuess(1.0));
        optimizer.setBounds(new SimpleBounds(0, 5));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 1.712132868;
        double expectedValue = -1.022352547;
        assertEquals("The optimization did not find the expected minimum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected minimum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with goal type.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithGoalType() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 4.353493396;
        double expectedValue = 1.373316047;
        assertEquals("The optimization did not find the expected maximum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected maximum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with goal type, initial guess, and bounds.
     * @throws Exception
     */
    @Test
    public void testDoOptimizeWithGoalTypeInitialGuessAndBounds() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setInitialGuess(new InitialGuess(1.0));
        optimizer.setBounds(new SimpleBounds(0, 5));
        UnivariatePointValuePair result = optimizer.optimize();
        double expectedX = 4.353493396;
        double expectedValue = 1.373316047;
        assertEquals("The optimization did not find the expected maximum", expectedX, result.getPoint(), 1e-7);
        assertEquals("The optimization did not find the expected maximum value", expectedValue, result.getValue(), 1e-7);
    }

    /**
     * Test doOptimize method with goal type, bounds, and a univariate function
     * which does not have a maximum in the bounds.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeWithGoalTypeBoundsAndNoMaximumFunction() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setBounds(new SimpleBounds(0, 1));
        optimizer.optimize();
    }
}