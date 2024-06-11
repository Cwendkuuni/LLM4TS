package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.PolynomialUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        // test default values
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
        assertNull(optimizer.jacobian);
        assertEquals(0, optimizer.cols);
        assertEquals(0, optimizer.rows);
        assertNull(optimizer.targetValues);
        assertNull(optimizer.residualsWeights);
        assertNull(optimizer.point);
        assertNull(optimizer.objective);
        assertNull(optimizer.residuals);
        assertEquals(0.0, optimizer.cost, 0.0);
        assertEquals(0, optimizer.iterations);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.objectiveEvaluations);
        assertEquals(0, optimizer.jacobianEvaluations);
        assertNull(optimizer.function);
        assertNull(optimizer.jF);

        // test setters
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertSame(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
        RealMatrix jacobianMatrix = new Array2DRowRealMatrix(new int[][]{{1, 2}, {3, 4}});
        optimizer.setJacobian(jacobianMatrix.getData());
        assertEquals(2, optimizer.cols);
        assertEquals(2, optimizer.rows);
        assertArrayEquals(jacobianMatrix.getData(), optimizer.jacobian);
        optimizer.setTargetValues(new double[]{1.0, 2.0});
        assertArrayEquals(new double[]{1.0, 2.0}, optimizer.targetValues);
        optimizer.setResidualsWeights(new double[]{0.5, 1.0});
        assertArrayEquals(new double[]{0.5, 1.0}, optimizer.residualsWeights);
        optimizer.setPoint(new double[]{3.0, 4.0});
        assertArrayEquals(new double[]{3.0, 4.0}, optimizer.point);
        optimizer.setObjective(new double[]{5.0, 6.0});
        assertArrayEquals(new double[]{5.0, 6.0}, optimizer.objective);
        optimizer.setResiduals(new double[]{1.0, 2.0});
        assertArrayEquals(new double[]{1.0, 2.0}, optimizer.residuals);
        optimizer.setCost(3.0);
        assertEquals(3.0, optimizer.cost, 0.0);
        optimizer.setIterations(5);
        assertEquals(5, optimizer.iterations);
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
        optimizer.setObjectiveEvaluations(3);
        assertEquals(3, optimizer.objectiveEvaluations);
        optimizer.setJacobianEvaluations(2);
        assertEquals(2, optimizer.jacobianEvaluations);
        MultivariateFunction f = new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2));
        optimizer.setFunction(f);
        assertSame(f, optimizer.function);
        optimizer.setJF(new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2).values(null, 2)));
        assertSame(f.jacobian(), optimizer.jF);
    }

    @Test
    public void testGetRMS() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        optimizer.setTargetValues(new double[]{1.0, 2.0});
        optimizer.setResidualsWeights(new double[]{0.5, 1.0});
        optimizer.setPoint(new double[]{3.0, 4.0});
        optimizer.setObjective(new double[]{2.5, 3.5});
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        assertEquals(Math.sqrt(2.25), optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        optimizer.setTargetValues(new double[]{1.0, 2.0});
        optimizer.setResidualsWeights(new double[]{0.5, 1.0});
        optimizer.setPoint(new double[]{3.0, 4.0});
        optimizer.setObjective(new double[]{2.5, 3.5});
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        assertEquals(0.5 * (1.0 * 1.0 + 1.0 * 1.0) + 1.0 * (1.5 * 1.5), optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        optimizer.setFunction(new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2)));
        optimizer.setJF(new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2).values(null, 2)));
        optimizer.setPoint(new double[]{3.0, 4.0});
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new int[][]{{0.04, -0.02}, {-0.02, 0.06}});
        RealMatrix covariances = optimizer.getCovariances();

        assertArrayEquals(expectedCovariances.getData(), covariances.getData());
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        optimizer.setFunction(new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2)));
        optimizer.setJF(new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2).values(null, 2)));
        optimizer.setPoint(new double[]{3.0, 4.0});
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        RealVector expectedErrors = new ArrayRealVector(new double[]{Math.sqrt(0.04), Math.sqrt(0.06)});
        RealVector errors = optimizer.guessParametersErrors();

        assertArrayEquals(expectedErrors.toArray(), errors.toArray());
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in unit tests");
                return null;
            }
        };

        MultivariateFunction f = new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2));
        double[] target = {1.0, 2.0};
        double[] weights = {0.5, 1.0};
        double[] startPoint = {3.0, 4.0};

        VectorialPointValuePair result = optimizer.optimize(f, target, weights, startPoint);

        assertEquals(5.0, result.getValue()[0], 0.001);
        assertEquals(5.0, result.getValue()[1], 0.001);
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 0.001);
        assertArrayEquals(new double[]{-0.5, 0.5}, optimizer.getResiduals(), 0.001);
        assertEquals(Math.sqrt(2.25), optimizer.cost, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_wrongTargetLength() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        MultivariateFunction f = new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2));
        double[] target = {1.0};
        double[] weights = {0.5, 1.0};
        double[] startPoint = {3.0, 4.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_wrongWeightsLength() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        MultivariateFunction f = new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2));
        double[] target = {1.0, 2.0};
        double[] weights = {0.5};
        double[] startPoint = {3.0, 4.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_wrongStartPointLength() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        MultivariateFunction f = new PolynomialFunction(PolynomialUtils.createCubicPolynomial(2));
        double[] target = {1.0, 2.0};
        double[] weights = {0.5, 1.0};
        double[] startPoint = {3.0};

        optimizer.optimize(f, target, weights, startPoint);
    }
}