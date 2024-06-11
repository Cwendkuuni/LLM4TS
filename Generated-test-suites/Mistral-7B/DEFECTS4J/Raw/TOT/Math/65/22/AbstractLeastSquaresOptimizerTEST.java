package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.vector.RealVector;
import org.apache.commons.math3.optim.vector.standard.ArrayRealVector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction function;

    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
        function = new SimpleVectorialFunction(new double[]{1, 2});
    }

    /**
     * Test optimization when the initial point is very close to the solution.
     */
    @Test
    public void testOptimizationWhenInitialPointIsClose() {
        double[] target = new double[]{3, 3};
        double[] weights = new double[]{1, 1};
        double[] startPoint = new double[]{2, 2};

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{3, 3}, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    /**
     * Test optimization when the initial point is very far from the solution.
     */
    @Test
    public void testOptimizationWhenInitialPointIsFar() {
        double[] target = new double[]{3, 3};
        double[] weights = new double[]{1, 1};
        double[] startPoint = new double[]{-5, -5};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail();
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test optimization when the function is not differentiable.
     */
    @Test
    public void testOptimizationWhenFunctionIsNotDifferentiable() {
        function = new SimpleVectorialFunction(new double[]{1, 2, 0}) {
            @Override
            public double value(RealVector x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public RealVector gradient(RealVector x) {
                throw new UnsupportedOperationException();
            }
        };

        double[] target = new double[]{3, 3, 3};
        double[] weights = new double[]{1, 1, 1};
        double[] startPoint = new double[]{1, 1, 1};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test optimization when the weight vector has different dimensions than the target vector.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWhenWeightsHaveWrongDimension() {
        double[] target = new double[]{3, 3};
        double[] weights = new double[]{1};
        double[] startPoint = new double[]{1, 1};

        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization when the target vector has different dimensions than the weights vector.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWhenTargetHaveWrongDimension() {
        double[] target = new double[]{3};
        double[] weights = new double[]{1, 1};
        double[] startPoint = new double[]{1, 1};

        optimizer.optimize(function, target, weights, startPoint);
    }

}