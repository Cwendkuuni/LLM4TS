import org.apache.commons.math3.analysis.MultivariateMatrixFunctions;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.ConstantFunction;
import org.apache.commons.math3.analysis.function.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.function.LinearFunction;
import org.apache.commons.math3.analysis.function.Product;
import org.apache.commons.math3.analysis.function.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        int maxIterations = 2;
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setMaxIterations(maxIterations);
        optimizer.setConvergenceChecker(checker);

        assertEquals(maxIterations, optimizer.getMaxIterations());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testUpdateResidualsAndCostWithSimpleFunction() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());

        double[] objective = optimizer.objective;
        double[] residuals = optimizer.residuals;

        assertEquals(-1.5, objective[0], 1e-6);
        assertEquals(-0.5, objective[1], 1e-6);
        assertEquals(0.5, residuals[0], 1e-6);
        assertEquals(1.0, residuals[1], 1e-6);

        double cost = optimizer.cost;
        double rms = optimizer.getRMS();

        assertEquals(2.5, cost, 1e-6);
        assertEquals(1.6128048e-1, rms, 1e-6);
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());

        double[][] jacobian = optimizer.jacobian;

        assertEquals(2, jacobian.length);
        assertEquals(2, jacobian[0].length);
        assertEquals(2, jacobian[1].length);

        assertEquals(-1.0, jacobian[0][0], 1e-6);
        assertEquals(1.0, jacobian[0][1], 1e-6);
        assertEquals(-1.0, jacobian[1][0], 1e-6);
        assertEquals(-1.0, jacobian[1][1], 1e-6);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());

        double[] errors = optimizer.guessParametersErrors();

        assertEquals(2, errors.length);
        assertEquals(1.6128048e-1, errors[0], 1e-6);
        assertEquals(1.6128048e-1, errors[1], 1e-6);
    }

    @Test
    public void testUpdateJacobianWithFunctionThrowsException() throws Exception {
        expectedEx.expect(FunctionEvaluationException.class);

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }

                    @Override
                    public double value(RealVector x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }

                    @Override
                    public double[] gradient(RealVector x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }

                    @Override
                    public double[] gradient(double[] x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }

                    @Override
                    public RealMatrix jacobian(RealVector x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }

                    @Override
                    public RealMatrix jacobian(double[] x) {
                        throw new FunctionEvaluationException("test exception", x);
                    }
                });

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());
    }

    @Test
    public void testDoOptimizeWithSimpleFunction() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        AbstractVectorialPointValuePair result = optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());

        RealVector optimalPoint = result.getPoint();
        double[] optimalValues = result.getValue();
        double[] errors = optimizer.guessParametersErrors();

        assertEquals(2, optimalPoint.getDimension());
        assertEquals(2, optimalValues.length);
        assertEquals(-1.5, optimalValues[0], 1e-6);
        assertEquals(-0.5, optimalValues[1], 1e-6);

        assertEquals(2, errors.length);
        assertEquals(1.6128048e-1, errors[0], 1e-6);
        assertEquals(1.6128048e-1, errors[1], 1e-6);
    }

    @Test
    public void testDoOptimizeWithSimpleFunctionAndLargeData() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        int n = 1000;
        int p = 2;

        RealVector startPoint = new Array2DRowRealMatrix(1, p).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(n, 1);

        for (int i = 0; i < n; ++i) {
            RealVector point = new Array2DRowRealMatrix(1, p).getColumnVector(0);
            double target = Polynomials.hermite(i, 1.0);
            targetMatrix.setEntry(i, 0, target);
            targetMatrix.addToEntry(i, 0, Math.random() * 0.1);
            startPoint.setEntry(0, i % p);
            startPoint.setEntry(1, (i - (i % 2)) / 2);
        }

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        AbstractVectorialPointValuePair result = optimizer.optimize(function, targetMatrix, new double[]{1.0, 1.0}, startPoint);

        RealVector optimalPoint = result.getPoint();
        double[] optimalValues = result.getValue();
        double[] errors = optimizer.guessParametersErrors();

        assertEquals(p, optimalPoint.getDimension());
        assertEquals(n, optimalValues.length);

        double rms = optimizer.getRMS();
        double chiSquare = optimizer.getChiSquare();

        double[] allErrors = new double[p];
        for (int i = 0; i < p; ++i) {
            allErrors[i] = errors[i];
        }

        Assert.assertArrayEquals(StatUtils.sort(allErrors), StatUtils.sort(StatUtils.sampleStandardNormal(p)), 1e-6);

        assertEquals(1.6128048e-1 * Math.sqrt(n), rms, 1e-6);
        assertTrue(chiSquare > 0);
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimizeWithSimpleFunctionAndMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(0);

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testDoOptimizeWithSimpleFunctionAndMaxEvaluationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxEvaluations(0);

        RealVector startPoint = new Array2DRowRealMatrix(1, 2).getColumnVector(0);
        RealMatrix targetMatrix = new Array2DRowRealMatrix(2, 1);
        targetMatrix.setEntry(0, 0, 1.0);
        targetMatrix.setEntry(1, 0, 2.0);

        DifferentiableMultivariateVectorFunction function = new Add(
                new Product(new LinearFunction(new double[]{-1.0, 1.0}), new LinearFunction(new double[]{1.0, -1.0})),
                new ConstantFunction(0.5));

        optimizer.optimize(function, targetMatrix.getColumnVector(0), new double[]{1.0, 1.0}, startPoint.toArray());
    }

}