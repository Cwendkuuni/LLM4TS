package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new NoSolutionException("No solution found.");
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DefaultLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSettersAndGetters() {
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
        optimizer.setConvergenceChecker(new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(double[] point, double[] objective) {
                return true;
            }
        });
        VectorialConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNotEquals(new SimpleVectorialValueChecker(), checker);
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxIterations() {
        optimizer.setMaxIterations(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxEvaluations() {
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testGetters() {
        assertEquals(DefaultLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.getIterations());
        assertEquals(0, optimizer.getEvaluations());
        assertEquals(0, optimizer.getJacobianEvaluations());
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianThrowsFunctionEvaluationException() throws Exception {
        optimizer.setFunction(new FunctionWithEvaluationException());
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianThrowsMaxEvaluationsExceededException() throws Exception {
        optimizer.setMaxEvaluations(1);
        optimizer.setFunction(new FunctionWithEvaluationException());
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianThrowsMaxIterationsExceededException() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.setFunction(new FunctionWithUpdateException());
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] objective = new double[]{0.5, 0.5};
        optimizer.setFunction(new FunctionForUpdateResidualsAndCost(target, weights, objective));
        optimizer.optimize(optimizer.getFunction(), new double[]{0.0, 0.0}, weights, new double[]{0.0, 0.0});
        optimizer.updateResidualsAndCost();
        double rms = optimizer.getRMS();
        double chiSquare = optimizer.getChiSquare();
        assertEquals(Math.sqrt(0.25), rms, 1e-6);
        assertEquals(0.5, chiSquare, 1e-6);
    }

    private static class FunctionForUpdateResidualsAndCost implements DifferentiableMultivariateVectorialFunction {
        private final double[] target;
        private final double[] weights;
        private final double[] objective;

        public FunctionForUpdateResidualsAndCost(double[] target, double[] weights, double[] objective) {
            this.target = target;
            this.weights = weights;
            this.objective = objective;
        }

        @Override
        public double[] value(double[] point) {
            double[] value = new double[target.length];
            System.arraycopy(objective, 0, value, 0, target.length);
            return value;
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                @Override
                public RealMatrix value(RealMatrix point) {
                    return new Array2DRowRealMatrix(jacobian(point.getData()));
                }

                private double[] jacobian(double[] point) {
                    double[] jacobian = new double[target.length][];
                    for (int i = 0; i < target.length; ++i) {
                        jacobian[i] = new double[]{point[i] - target[i]};
                    }
                    return jacobian;
                }
            };
        }
    }

    private static class FunctionWithEvaluationException implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Function evaluation exception", point);
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return null;
        }
    }

    private static class FunctionWithUpdateException implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] point) {
            return new double[]{0.0};
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                @Override
                public RealMatrix value(RealMatrix point) {
                    throw new MaxIterationsExceededException("Max iterations exceeded");
                }
            };
        }
    }
}