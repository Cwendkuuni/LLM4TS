package org.apache.commons.math.optimization.general; // import necessary classes

import org.apache.commons.math3.optim.AbstractMultivariateOptimizerTest;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest extends AbstractMultivariateOptimizerTest {

    

    @Test
    public void testAbstractLeastSquaresOptimizer_DefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testAbstractLeastSquaresOptimizer_UpdateJacobian_ThrowsException() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
                                                           OptimizationException, IllegalArgumentException {
                throw new RuntimeException("Not implemented");
            }
        };

        try {
            optimizer.updateJacobian();
            Assert.fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractLeastSquaresOptimizer_UpdateJacobian() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
                                                           OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new double[]{0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 0.0});
            }
        };

        try {
            optimizer.updateJacobian();
            Assert.assertArrayNotEquals(new double[0][], optimizer.jacobian);
        } catch (FunctionEvaluationException e) {
            Assert.fail("Unexpected FunctionEvaluationException");
        }
    }

    @Test
    public void testAbstractLeastSquaresOptimizer_UpdateResidualsAndCost() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
                                                           OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new double[]{0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 0.0});
            }
        };

        try {
            optimizer.updateResidualsAndCost();
            Assert.assertArrayNotEquals(new double[0], optimizer.residuals);
            Assert.assertEquals(0.0, optimizer.cost, 0.01);
        } catch (FunctionEvaluationException e) {
            Assert.fail("Unexpected FunctionEvaluationException");
        }
    }

    @Test(expected = OptimizationException.class)
    public void testAbstractLeastSquaresOptimizer_MaxIterationsExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
                                                           OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new double[]{0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 0.0});
            }
        };

        optimizer.setMaxIterations(0);
        optimizer.optimize(new MockFunction(), new double[]{1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0},
                new double[]{0.0, 0.0, 0.0});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testAbstractLeastSquaresOptimizer_MaxEvaluationsExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
                                                           OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new double[]{0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 0.0});
            }
        };

        optimizer.setMaxEvaluations(0);
        optimizer.optimize(new MockFunction(), new double[]{1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0},
                new double[]{0.0, 0.0, 0.0});
    }

    
}