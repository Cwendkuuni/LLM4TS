package org.apache.commons.math.optimization.general.test;

import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.VectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FisherFalfeldt;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(50);
    }

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new TestAbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(20);
        optimizer.setMaxEvaluations(30);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        assertEquals(20, optimizer.getMaxIterations());
        assertEquals(30, optimizer.getMaxEvaluations());
        assertEquals(1e-6, optimizer.getConvergenceChecker().getTolerance(), 1e-9);
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        optimizer.updateJacobian();
        assertNotNull(optimizer.jacobian);
        assertEquals(2, optimizer.jacobian.length);
        assertEquals(2, optimizer.jacobian[0].length);
        assertEquals(2, optimizer.jacobian[1].length);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianThrowsFunctionEvaluationException() throws Exception {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(function, new double[]{0.0}, null, new double[]{0.0});
        optimizer.updateJacobian();
    }

    @Test
    public void testGetRMS() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double expectedRMS = Math.sqrt(0.5);
        assertEquals(expectedRMS, optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double expectedChiSquare = 0.5;
        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGetCovariances() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double[][] expectedCovariances = {
            {0.025, 0.017},
            {0.017, 0.025}
        };
        double[][] actualCovariances = optimizer.getCovariances();
        for (int i = 0; i < actualCovariances.length; i++) {
            for (int j = 0; j < actualCovariances[i].length; j++) {
                assertEquals(expectedCovariances[i][j], actualCovariances[i][j], 1e-6);
            }
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGetCovariancesThrowsFunctionEvaluationException() throws Exception {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(function, new double[]{0.0}, null, new double[]{0.0});
        optimizer.getCovariances();
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double[] expectedErrors = {0.121, 0.121};
        double[] actualErrors = optimizer.guessParametersErrors();
        for (int i = 0; i < actualErrors.length; i++) {
            assertEquals(expectedErrors[i], actualErrors[i], 1e-6);
        }
    }

    @Test
    public void testIterationsAndEvaluationsCounter() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        assertEquals(1, optimizer.getIterations());
        assertEquals(12, optimizer.getEvaluations());

        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        assertEquals(2, optimizer.getIterations());
        assertEquals(23, optimizer.getEvaluations());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testIterationsLimitReached() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testEvaluationsLimitReached() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
    }

    @Test
    public void testOptimize() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        PointValuePair optimizedPoint = optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double[] expectedOptimizedPoint = {0.728, 1.171};
        assertEquals(expectedOptimizedPoint[0], optimizedPoint.getPoint()[0], 1e-6);
        assertEquals(expectedOptimizedPoint[1], optimizedPoint.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithCustomChecker() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.setMaxIterations(1);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-1));
        PointValuePair optimizedPoint = optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double[] expectedOptimizedPoint = {0.536, 0.858};
        assertEquals(expectedOptimizedPoint[0], optimizedPoint.getPoint()[0], 1e-6);
        assertEquals(expectedOptimizedPoint[1], optimizedPoint.getValue(), 1e-6);
        assertFalse(optimizer.isDone());
    }

    @Test
    public void testOptimizeWithGoalType() throws Exception {
        ObjectiveFunction function = new FisherFalfeldt(2);
        double[] startPoint = {1.0, 1.0};
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair optimizedPoint = optimizer.optimize(function, new double[]{1.0, 1.0}, null, startPoint);
        double[] expectedOptimizedPoint = {0.728, 1.171};
        assertEquals(expectedOptimizedPoint[0], optimizedPoint.getPoint()[0], 1e-6);
        assertEquals(expectedOptimizedPoint[1], optimizedPoint.getValue(), 1e-6);
    }

    @Test
    public void testThrowExceptionIfMaxIterationsOrEvaluationsExceeded() throws Exception {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return 0;
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setMaxIterations(0);
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, new double[]{0.0}, null, new double[]{0.0});
    }
}

class TestAbstractLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    @Override
    protected VectorialPointValuePair doOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        return super.optimize(new FisherFalfeldt(2), new double[]{1.0, 1.0}, null, new double[]{1.0, 1.0});
    }
}