package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction function;
    private double[] target;
    private double[] weights;
    private double[] startPoint;
    private double[] expectedSolution;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException();
            }
        };
        function = new MockDifferentiableMultivariateVectorialFunction(3, 2);
        target = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{1.0, 1.0, 1.0};
        startPoint = new double[]{0.0, 0.0, 0.0};
        expectedSolution = new double[]{1.5, 2.5, 3.5};
    }

    @Test
    public void testOptimize_Basic() throws Exception {
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        assertThat(result.getPoint(), notNullValue());
        assertThat(result.getValue(), notNullValue());
        Assert.assertArrayEquals(expectedSolution, result.getPoint(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_MaxIterations() throws Exception {
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimize_MaxEvaluations() throws Exception {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test
    public void testOptimize_InvalidArguments() throws Exception {
        // target and weights arrays have different lengths
        target = new double[]{1.0, 2.0};
        weights = new double[]{1.0, 2.0, 3.0};
        Assert.assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, target, weights, startPoint));

        // start point array has wrong dimension
        startPoint = new double[]{0.0, 0.0};
        Assert.assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, target, weights, startPoint));

        // target and weights arrays have same length but different dimensions than the function
        target = new double[]{1.0, 2.0};
        weights = new double[]{1.0};
        function = new MockDifferentiableMultivariateVectorialFunction(2, 3);
        Assert.assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, target, weights, startPoint));
    }

    @Test
    public void testGetRMS() throws Exception {
        double[] solution = optimizer.optimize(function, target, weights, startPoint).getPoint();
        double rms = optimizer.getRMS();
        double error = 0.1;
        double[] errorSolution = new double[solution.length];
        for (int i = 0; i < solution.length; i++) {
            errorSolution[i] = solution[i] + error;
        }
        double[] newTarget = target.clone();
        for (int i = 0; i < target.length; i++) {
            newTarget[i] += error * weights[i];
        }
        DifferentiableMultivariateVectorialFunction newFunction = new MockDifferentiableMultivariateVectorialFunction(3, 2);
        VectorialPointValuePair newResult = optimizer.optimize(newFunction, newTarget, weights, startPoint);
        double newRms = optimizer.getRMS();
        double tolerance = 0.01;
        assertThat(rms, is(Math.sqrt(Math.pow(error, 2) * Math.sum(weights))));
        assertThat(newRms, is(Math.sqrt(Math.pow(error, 2) * Math.sum(weights))));
    }

    @Test
    public void testGetChiSquare() throws Exception {
        double[] solution = optimizer.optimize(function, target, weights, startPoint).getPoint();
        double chiSquare = optimizer.getChiSquare();
        double error = 0.1;
        double[] errorSolution = new double[solution.length];
        for (int i = 0; i < solution.length; i++) {
            errorSolution[i] = solution[i] + error;
        }
        double[] newTarget = target.clone();
        for (int i = 0; i < target.length; i++) {
            newTarget[i] += error * weights[i];
        }
        DifferentiableMultivariateVectorialFunction newFunction = new MockDifferentiableMultivariateVectorialFunction(3, 2);
        VectorialPointValuePair newResult = optimizer.optimize(newFunction, newTarget, weights, startPoint);
        double newChiSquare = optimizer.getChiSquare();
        double tolerance = 0.01;
        double chiSquareExpected = Math.pow(error, 2) * Math.sum(weights);
        assertThat(chiSquare, is(chiSquareExpected));
        assertThat(newChiSquare, is(chiSquareExpected));
    }

    @Test
    public void testGetCovariances() throws Exception {
        double[] solution = optimizer.optimize(function, target, weights, startPoint).getPoint();
        double[][] covariances = optimizer.getCovariances();
        double error = 0.1;
        double[] errorSolution = new double[solution.length];
        for (int i = 0; i < solution.length; i++) {
            errorSolution[i] = solution[i] + error;
        }
        double[] newTarget = target.clone();
        for (int i = 0; i < target.length; i++) {
            newTarget[i] += error * weights[i];
        }
        DifferentiableMultivariateVectorialFunction newFunction = new MockDifferentiableMultivariateVectorialFunction(3, 2);
        VectorialPointValuePair newResult = optimizer.optimize(newFunction, newTarget, weights, startPoint);
        double[][] newCovariances = optimizer.getCovariances();
        double tolerance = 0.01;
        double[][] expectedCovariances = new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        for (int i = 0; i < covariances[0].length; i++) {
            for (int j = 0; j < covariances.length; j++) {
                assertThat(covariances[i][j], is(expectedCovariances[i][j] + tolerance));
            }
        }
        for (int i = 0; i < newCovariances[0].length; i++) {
            for (int j = 0; j < newCovariances.length; j++) {
                assertThat(newCovariances[i][j], is(expectedCovariances[i][j] + tolerance));
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        double[] solution = optimizer.optimize(function, target, weights, startPoint).getPoint();
        double[] errors = optimizer.guessParametersErrors();
        double tolerance = 0.01;
        for (int i = 0; i < solution.length; i++) {
            assertThat(errors[i], is(Math.sqrt(Math.pow(0.1, 2) * Math.sum(weights)) * Math.sqrt(1.0 + Math.pow(i, 2)) + tolerance));
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobian_Exception() throws Exception {
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobian_SingularProblem() throws Exception {
        double[][] covariances = new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
        };
        optimizer.setCovariances(covariances);
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        optimizer.updateJacobian();
        double[][] jacobian = optimizer.getJacobian();
        int rows = jacobian.length;
        int cols = jacobian[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                assertThat(jacobian[i][j], is(0.0));
            }
        }
    }

    @Test
    public void testGetIterations() throws Exception {
        int iterations = optimizer.getIterations();
        int expectedIterations = 0;
        Assert.assertEquals(expectedIterations, iterations);
    }

    @Test
    public void testGetMaxIterations() throws Exception {
        int maxIterations = optimizer.getMaxIterations();
        int expectedMaxIterations = 100;
        Assert.assertEquals(expectedMaxIterations, maxIterations);
    }

    @Test
    public void testSetMaxIterations() throws Exception {
        optimizer.setMaxIterations(42);
        int maxIterations = optimizer.getMaxIterations();
        int expectedMaxIterations = 42;
        Assert.assertEquals(expectedMaxIterations, maxIterations);
    }

    @Test
    public void testGetMaxEvaluations() throws Exception {
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = Integer.MAX_VALUE;
        Assert.assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testSetMaxEvaluations() throws Exception {
        optimizer.setMaxEvaluations(123);
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 123;
        Assert.assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() throws Exception {
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 0;
        Assert.assertEquals(expectedEvaluations, evaluations);
    }

    @Test
    public void testSetConvergenceChecker() throws Exception {
        VectorialConvergenceChecker checker = new MockVectorialConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        VectorialConvergenceChecker actualChecker = optimizer.getConvergenceChecker();
        Assert.assertEquals(checker, actualChecker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConvergenceChecker_Null() throws Exception {
        optimizer.setConvergenceChecker(null);
    }
}

class MockDifferentiableMultivariateVectorialFunction implements DifferentiableMultivariateVectorialFunction {

    private final int rows;
    private final int cols;

    public MockDifferentiableMultivariateVectorialFunction(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public double[] value(double[] x) throws FunctionEvaluationException {
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            result[i] = Math.pow(x[i], 2);
        }
        return result;
    }

    @Override
    public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
        return new MockMultivariateMatrixFunction(rows, cols);
    }
}

class MockMultivariateMatrixFunction implements MultivariateMatrixFunction {

    private final int rows;
    private final int cols;

    public MockMultivariateMatrixFunction(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public RealMatrix value(RealMatrix x) throws FunctionEvaluationException {
        RealMatrix result = MatrixUtils.createRealMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setEntry(i, j, Math.pow(x.getEntry(i, j), 2));
            }
        }
        return result;
    }
}

class MockVectorialConvergenceChecker implements VectorialConvergenceChecker {

    @Override
    public boolean isConverged(double[] x, double[] fx, double eps) {
        return Math.abs(fx[0] - fx[1]) < 0.001;
    }

}