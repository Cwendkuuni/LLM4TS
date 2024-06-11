package org.apache.commons.math.optimization.general.test;

import org.apache.commons.math3.optim.AbstractMultivariateOptimizerTest;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestAbstractLeastSquaresOptimizer extends AbstractMultivariateOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertEquals(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounterMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        optimizer.updateJacobian();
        assertNotNull(optimizer.jacobian);
        assertEquals(optimizer.rows, optimizer.jacobian.length);
        assertEquals(optimizer.cols, optimizer.jacobian[0].length);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                throw new FunctionEvaluationException(new Object[]{"test"}, new Object[]{});
            }
        };
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                incrementIterationsCounter();
                throw new OptimizationException(new MaxIterationsExceededException(1));
            }
        };
        optimizer.setMaxIterations(0);
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        optimizer.updateResidualsAndCost();
        double expectedCost = Math.sqrt(5.0);
        assertEquals(expectedCost, optimizer.cost, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                throw new FunctionEvaluationException(new Object[]{"test"}, new Object[]{});
            }
        };
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.updateResidualsAndCost();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateResidualsAndCostMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                incrementIterationsCounter();
                throw new OptimizationException(new MaxIterationsExceededException(1));
            }
        };
        optimizer.setMaxIterations(0);
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        double expectedRMS = Math.sqrt(2.5);
        assertEquals(expectedRMS, optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        double expectedChiSquare = 5.0;
        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGetCovariances() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        double[][] expectedCovariances = new double[][]{
                {1.000231272855112, 0.000822029782227},
                {0.000822029782227, 1.00025513122917}
        };
        double[][] covariances = optimizer.getCovariances();
        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGetCovariancesFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                throw new FunctionEvaluationException(new Object[]{"test"}, new Object[]{});
            }
        };
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.getCovariances();
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariancesSingularProblem() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize() throws Exception {
                throw new OptimizationException(new IllegalStateException("Test"));
            }
        };
        optimizer.optimize(new SineFunction(), new double[]{0.0}, new double[]{1.0}, new double[]{0.0});
        optimizer.getCovariances();
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DoubleFunction function = functions.ROSENBROCK;
        PointValuePair initialPoint = new PointValuePair(1.0, 1.0);
        double[] target = {2.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.optimize(function, target, weights, initialPoint.getPoint());
        double[] expectedErrors = new double[]{1.110234718379842, 0.555117359189921};
        double[] errors = optimizer.guessParametersErrors();
        assertArrayEquals(expectedErrors, errors);
    }

}