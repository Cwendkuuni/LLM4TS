package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGettersAndSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // this method is not relevant for testing getters and setters
                return null;
            }
        };

        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(10);
        assertEquals(5, optimizer.getMaxIterations());
        assertEquals(10, optimizer.getMaxEvaluations());

        assertNull(optimizer.getFunction());
        optimizer.setFunction(new MockFunction());
        assertNotNull(optimizer.getFunction());

        assertNull(optimizer.getJF());
        optimizer.setJF(new MockJacobianFunction());
        assertNotNull(optimizer.getJF());

        assertArrayEquals(new double[0][0], optimizer.getJacobian());
        optimizer.updateJacobian();
        assertNotNull(optimizer.getJacobian());

        assertNull(optimizer.getPoint());
        optimizer.setPoint(new ArrayRealVector(new double[]{}));
        assertNotNull(optimizer.getPoint());

        assertNull(optimizer.getObjective());
        optimizer.updateObjective();
        assertNotNull(optimizer.getObjective());

        assertNull(optimizer.getResiduals());
        optimizer.updateResidualsAndCost();
        assertNotNull(optimizer.getResiduals());

        assertNull(optimizer.getTargetValues());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        assertNotNull(optimizer.getTargetValues());

        assertNull(optimizer.getResidualsWeights());
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        assertNotNull(optimizer.getResidualsWeights());

        assertNull(optimizer.getCost());
        optimizer.updateCost();
        assertEquals(0, optimizer.getCost(), 1e-9);

        assertNull(optimizer.getCovariances());
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        try {
            optimizer.getCovariances();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        RealMatrix covariances = optimizer.getCovariances();
        assertNotNull(covariances);

        optimizer.setConvergenceChecker(new MockConvergenceChecker());
        assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testGetRms() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setPoint(new ArrayRealVector(new double[]{0, 0, 0}));
        optimizer.updateObjective();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        assertEquals(Math.sqrt(6), optimizer.getRMS(), 1e-9);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setPoint(new ArrayRealVector(new double[]{0, 0, 0}));
        optimizer.updateObjective();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        assertEquals(12.0, optimizer.getChiSquare(), 1e-9);
    }

    @Test
    public void testGetCovariances()
            throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        optimizer.setPoint(new ArrayRealVector(new double[]{0, 0, 0}));
        optimizer.updateObjective();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        RealMatrix covariances = optimizer.getCovariances();
        assertNotNull(covariances);
        RealArray2DRowAccessor covariancesAccessor = (RealArray2DRowAccessor) covariances;
        assertEquals(1.0, covariancesAccessor.getEntry(0, 0), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(0, 1), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(0, 2), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(1, 0), 1e-9);
        assertEquals(1.0, covariancesAccessor.getEntry(1, 1), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(1, 2), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(2, 0), 1e-9);
        assertEquals(0.0, covariancesAccessor.getEntry(2, 1), 1e-9);
        assertEquals(1.0, covariancesAccessor.getEntry(2, 2), 1e-9);
    }

    @Test
    public void testGuessParametersErrors()
            throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        optimizer.setPoint(new ArrayRealVector(new double[]{0, 0, 0}));
        optimizer.updateObjective();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();

        double[] errors = optimizer.guessParametersErrors();
        assertNotNull(errors);
        assertEquals(1.0, errors[0], 1e-9);
        assertEquals(1.0, errors[1], 1e-9);
        assertEquals(1.0, errors[2], 1e-9);
    }

    @Test
    public void testOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new ArrayRealVector(new double[]{0, 0, 0}), new ArrayRealVector(new double[]{0, 0, 0}));
            }
        };

        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(10);
        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        ArrayRealVector startPoint = new ArrayRealVector(new double[]{0.1, 0.2, 0.3});
        VectorialPointValuePair result = optimizer.optimize(optimizer.getFunction(), optimizer.getTargetValues(),
                optimizer.getResidualsWeights(), startPoint.toArray());

        assertTrue(result.isSuccess());
        assertArrayEquals(new double[]{0.01, -0.02, 0.03}, result.getPoint().toArray(), 1e-9);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getValue().toArray(), 1e-9);
        assertEquals(0, optimizer.getIterations());
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithWrongStartPointDimension() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(10);
        optimizer.setFunction(new MockFunction());
        optimizer.setJF(new MockJacobianFunction());
        optimizer.setTargetValues(new double[]{1, 2, 3});
        optimizer.setResidualsWeights(new double[]{1, 2, 3});
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        ArrayRealVector startPoint = new ArrayRealVector(new double[]{0.1, 0.2});
        optimizer.optimize(optimizer.getFunction(), optimizer.getTargetValues(),
                optimizer.getResidualsWeights(), startPoint.toArray());
    }

    private static class MockFunction implements DifferentiableMultivariateVectorialFunction {
        @Override
        public RealVector value(RealVector x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RealMatrix jacobian(RealVector x)
                throws FunctionEvaluationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class MockJacobianFunction implements MultivariateMatrixFunction {
        @Override
        public RealMatrix value(RealVector x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class MockConvergenceChecker implements VectorialConvergenceChecker {
        @Override
        public boolean isConverged(RealVector x, RealVector fx, RealVector xNew) {
            return false;
        }
    }

}