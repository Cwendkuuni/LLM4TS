package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSampleData;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    /** Optimizer to test. */
    private UnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    /** Test function. */
    private UnivariateRealFunction testFunction;
    /** Test data. */
    private GoalTypeSampleData testData;
    /** Random generator. */
    private RandomGenerator generator;

    @Before
    public void setUp() {
        testFunction = new PolynomialFunctions.SinusFunction();
        optimizer = new MultiStartUnivariateRealOptimizer<>(new SimpleUnivariateRealOptimizer(testFunction), 5, new FakeRandomGenerator());
        testData = new GoalTypeSampleData(GoalType.MINIMIZE, testFunction, -1.0, 1.0, 10);
    }

    /**
     * Test that the optimizer returns the expected minimum for a simple function.
     */
    @Test
    public void testSimpleMin() {
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 1.0);
        assertEquals(-1.0, result.getPoint().doubleValue(), 1e-9);
    }

    /**
     * Test that the optimizer returns the expected maximum for a simple function.
     */
    @Test
    public void testSimpleMax() {
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -1.0, 1.0);
        assertEquals(1.0, result.getPoint().doubleValue(), 1e-9);
    }

    /**
     * Test that the optimizer returns null if the underlying optimizer does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        PointValuePair result = optimizer.optimize(new PolynomialFunctions.LogisticFunction(), GoalType.MINIMIZE, -1.0, 1.0);
        assertNull(result);
    }

    /**
     * Test that the optimizer returns the expected optima for a hard function.
     */
    @Test
    public void testHardFunction() {
        PointValuePair[] optima = optimizer.getOptima();
        assertEquals(5, optima.length);
        for (PointValuePair pair : optima) {
            assertNotNull(pair);
            if (pair != null) {
                assertEquals(-1.0 + Math.PI * i / 4.0, pair.getPoint().doubleValue(), 1e-9);
                assertEquals(-Math.cos(Math.PI * i / 4.0), pair.getValue(), 1e-9);
            }
        }
    }
}

class SimpleUnivariateRealOptimizer<FUNC extends UnivariateRealFunction>
    implements BaseUnivariateRealOptimizer<FUNC> {
    /** Function to optimize. */
    private FUNC function;

    /**
     * Create a simple optimizer from a function.
     * @param function Function to optimize.
     */
    public SimpleUnivariateRealOptimizer(FUNC function) {
        this.function = function;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PointValuePair> checker) {
        // Not used.
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return null;
    }

    @Override
    public int getMaxEvaluations() {
        return 10;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        // Not used.
    }

    @Override
    public PointValuePair optimize(FUNC f, GoalType goal, double start, double end)
        throws FunctionEvaluationException {
        PointValuePair result = new PointValuePair(start, f.value(start));
        if (goal == GoalType.MINIMIZE) {
            if (result.getValue() > 0.0) {
                throw new FunctionEvaluationException("Minimum not found.");
            }
        } else {
            if (result.getValue() < 0.0) {
                throw new FunctionEvaluationException("Maximum not found.");
            }
        }
        return result;
    }
}

class FakeRandomGenerator implements RandomGenerator {
    @Override
    public double nextDouble() {
        return 0.5;
    }
}