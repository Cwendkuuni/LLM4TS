import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.AbstractNonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.gradient.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private MultivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // empty implementation
                return new PointValuePair(new double[1], 0);
            }
        };
        function = new GaussNewtonOptimizer(optimizer) {
            private static final double EPSILON = 1e-14;

            @Override
            protected ConvergenceChecker getConvergenceChecker() {
                return new SimpleValueChecker(EPSILON);
            }
        };
    }

    @Test
    public void testGetters() {
        assertArrayEquals(new int[]{AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS,
                AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS}, optimizer.getMaxIterations(),
                optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
        optimizer.setMaxIterations(-5);
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());

        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(2, optimizer.getIterations());

        optimizer.setMaxEvaluations(3);
        assertEquals(0, optimizer.getEvaluations());
        optimizer.evaluateObjectiveFunction(new double[1]);
        optimizer.evaluateJacobian(new double[1]);
        optimizer.evaluateObjectiveFunction(new double[1]);
        assertEquals(3, optimizer.getEvaluations());

        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6));
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testUpdateJacobianAndResiduals() throws Exception {
        double[] target = new double[]{0.1, 0.2};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};
        double[] expectedJacobian = new double[][]{
                {-0.1, -0.1},
                {-0.2, -0.2}
        };
        double[] expectedResiduals = new double[]{0, 0};

        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, target, weights, startPoint);

        updateJacobianAndResiduals(optimizer, target, weights, startPoint);

        assertArrayEquals(expectedJacobian, optimizer.jacobian);
        assertArrayEquals(expectedResiduals, optimizer.residuals);
        assertEquals(2, optimizer.getJacobianEvaluations());
        assertEquals(2, optimizer.getObjectiveEvaluations());
    }

    @Test
    public void testGetRMSAndChiSquare() throws Exception {
        double[] target = new double[]{0.1, 0.2};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};

        optimizer.optimize(function, target, weights, startPoint);

        assertEquals(0.14142137, optimizer.getRMS(), 1e-8);
        assertEquals(0.48, optimizer.getChiSquare(), 1e-8);
    }

    @Test
    public void testGetCovariances() throws Exception {
        double[] target = new double[]{0.1, 0.2};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};

        optimizer.optimize(function, target, weights, startPoint);

        double[][] expectedCovariances = new double[][]{
                {0.02081326, 0.01147229},
                {0.01147229, 0.02081326}
        };

        double[][] covariances = optimizer.getCovariances();
        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        double[] target = new double[]{0.1, 0.2};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};

        optimizer.optimize(function, target, weights, startPoint);

        double[] expectedErrors = new double[]{0.04524144, 0.04524144};

        double[] errors = optimizer.guessParametersErrors();
        assertArrayEquals(expectedErrors, errors);
    }

    private void updateJacobianAndResiduals(AbstractLeastSquaresOptimizer optimizer, double[] target,
                                          double[] weights, double[] startPoint) throws Exception {
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
    }

}