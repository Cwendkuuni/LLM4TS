package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.noderiv.BFGS;
import org.apache.commons.math3.optim.nonlinear.Gradient;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for AbstractLeastSquaresOptimizer class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private static final int MAX_ITERATIONS = 10;
    private static final int MAX_EVALUATIONS = 10;
    private static final double EPSILON = 1e-6;

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                return new BFGS(new SimpleVectorialFunction(new double[] {1.0, 1.0}),
                        new Gradient(new SimpleVectorialFunction(new double[] {1.0, 1.0})))
                        .optimize(new ArrayRealVector(new double[] {2.0, 2.0}), EPSILON, MAX_ITERATIONS,
                                MAX_EVALUATIONS);
            }
        };
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(EPSILON));
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(MAX_ITERATIONS);
        assertEquals(MAX_ITERATIONS, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        assertEquals(MAX_EVALUATIONS, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_EVALUATIONS, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetIterations() {
        optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), new double[] {2.0, 2.0},
                new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(0, optimizer.getIterations());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        optimizer.setMaxIterations(0);
        optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), new double[] {2.0, 2.0},
                new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), new double[] {2.0, 2.0},
                new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), new double[] {2.0, 2.0},
                new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), new double[] {2.0, 2.0},
                new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(0, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testGetObjective() {
        double[] startPoint = new double[] {1.0, 1.0};
        double[] target = new double[] {2.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), target,
                weights, startPoint);
        double[] objective = result.getPoint();
        assertArrayEquals(new double[] {0.0, 0.0}, objective, 0.0);
    }

    @Test
    public void testGetRMS() {
        double[] startPoint = new double[] {1.0, 1.0};
        double[] target = new double[] {2.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), target,
                weights, startPoint);
        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(2.0), rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        double[] startPoint = new double[] {1.0, 1.0};
        double[] target = new double[] {2.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), target,
                weights, startPoint);
        double chiSquare = optimizer.getChiSquare();
        assertEquals(2.0, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] startPoint = new double[] {1.0, 1.0};
        double[] target = new double[] {2.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), target,
                weights, startPoint);
        double[][] covariances = optimizer.getCovariances();
        RealMatrix expectedCovariances = MatrixUtils.createRealMatrix(new double[][] {{0.007812564, 0.007812564},
                {0.007812564, 0.007812564}});
        assertEquals(expectedCovariances.toString(), covariances[0][0], covariances[0][1], 0.0);
        assertEquals(covariances[1][0], covariances[0][1], 0.0);
        assertEquals(covariances[1][1], covariances[0][0], 0.0);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] startPoint = new double[] {1.0, 1.0};
        double[] target = new double[] {2.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new SimpleVectorialFunction(new double[] {1.0, 1.0}), target,
                weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        assertArrayEquals(new double[] {0.14142137, 0.14142137}, errors, 0.0);
    }

}