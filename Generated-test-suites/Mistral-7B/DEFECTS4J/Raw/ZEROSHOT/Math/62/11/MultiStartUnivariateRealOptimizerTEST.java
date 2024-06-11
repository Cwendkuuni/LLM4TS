package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Objective function for testing. */
    private ObjectiveFunction f;
    /** Base optimizer for testing. */
    private BaseUnivariateRealOptimizer<ObjectiveFunction> baseOptimizer;
    /** Multi-start optimizer for testing. */
    private MultiStartUnivariateRealOptimizer<ObjectiveFunction> multiStartOptimizer;
    /** Convergence checker for testing. */
    private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
    /** Random number generator for testing. */
    private RandomGenerator randomGenerator;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        f = new PolynomialFunction(5, new double[]{-1.0, 1.0, -1.0, 1.0, 1.0});
        baseOptimizer = new BrentMinimizer();
        convergenceChecker = new MaxEvalExceededChecker(1000);
        randomGenerator = new MersenneTwister();
    }

    /**
     * Tests multi-start optimization with a simple objective function.
     */
    @Test
    public void testMinimizeSimpleFunction() {
        final int starts = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(
                baseOptimizer, starts, randomGenerator);
        UnivariateRealPointValuePair result = null;
        try {
            result = multiStart.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        } catch (FunctionEvaluationException | NoBracketingException e) {
            fail(e.getMessage());
        }

        assertNotNull(result);
        assertEquals(f.value(result.getPoint()), result.getValue(), 1e-8);

        UnivariateRealPointValuePair[] optima = multiStart.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair op : optima) {
            assertNotNull(op);
            if (op != null) {
                assertEquals(f.value(op.getPoint()), op.getValue(), 1e-8);
            }
        }
    }

    /**
     * Tests multi-start optimization for a function with multiple local minima.
     */
    @Test
    public void testMinimizeFunctionWithMultipleLocalMinima() {
        final int starts = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(
                baseOptimizer, starts, randomGenerator);

        // Objective function for testing.
        class MyFunction implements ObjectiveFunction {
            public double value(double x) {
                if (x > 0.5) {
                    return Math.pow(x - 0.5, 2);
                } else {
                    return Math.pow(x + 0.5, 2);
                }
            }

            public double value(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getNumberOfVariables() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        MyFunction myFunction = new MyFunction();
        UnivariateRealPointValuePair result = null;
        try {
            result = multiStart.optimize(myFunction, GoalType.MINIMIZE, -5.0, 5.0);
        } catch (FunctionEvaluationException | NoBracketingException | ConvergenceException e) {
            fail(e.getMessage());
        }

        assertNotNull(result);
        assertEquals(myFunction.value(result.getPoint()), result.getValue(), 1e-8);

        UnivariateRealPointValuePair[] optima = multiStart.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair op : optima) {
            assertNotNull(op);
            if (op != null) {
                assertEquals(myFunction.value(op.getPoint()), op.getValue(), 1e-8);
            }
        }
    }

    /**
     * Test convergence checker in MultiStartUnivariateRealOptimizer.
     */
    @Test
    public void testMultiStartWithConvergenceChecker() {
        final int starts = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(
                baseOptimizer, starts, randomGenerator);
        multiStart.setConvergenceChecker(convergenceChecker);

        // Objective function for testing.
        class MyFunction implements ObjectiveFunction {
            public double value(double x) {
                return Math.pow(x - 2.0, 2);
            }

            public double value(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getNumberOfVariables() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        MyFunction myFunction = new MyFunction();

        try {
            multiStart.optimize(myFunction, GoalType.MINIMIZE, -5.0, 5.0);
            fail("Expected convergence exception.");
        } catch (ConvergenceException e) {
            // Expected
        }

        UnivariateRealPointValuePair[] optima = multiStart.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair op : optima) {
            if (op != null) {
                assertFalse(convergenceChecker.isConverged(op));
            }
        }
    }
}

/**
 * Convergence checker for testing.
 */
class MaxEvalExceededChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
    private final int maxEvaluations;

    /**
     * Creates a new MaxEvalExceededChecker instance.
     *
     * @param maxEvaluations Maximum number of function evaluations allowed.
     */
    public MaxEvalExceededChecker(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public boolean isConverged(UnivariateRealPointValuePair pointValuePair) {
        if (pointValuePair == null) {
            return true;
        }
        return (pointValuePair.getEvaluations() >= maxEvaluations);
    }
}