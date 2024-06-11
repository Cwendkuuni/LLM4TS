import org.apache.commons.math3.optim.AbstractOptimizerTest;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.differentiable.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.optim.differentiable.JacobianVectorFunction;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractLeastSquaresOptimizerTest extends AbstractOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertEquals(5, optimizer.getMaxIterations());
        assertEquals(AbstractLeastSquaresOptimizer.class.getName() + "#" + AbstractLeastSquaresOptimizer.class.getSimpleName() + ".checker", optimizer.getConvergenceChecker().getClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsNegative() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-1);
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{0.0});
        optimizer.updateJacobian();
        assertEquals(0, optimizer.getJacobianEvaluations());
        assertEquals(Math.pow(2, optimizer.getRows()), optimizer.jacobian.length);
        assertEquals(Math.pow(2, optimizer.getCols()), optimizer.jacobian[0].length);
        for (int i = 0; i < optimizer.jacobian.length; i++) {
            for (int j = 0; j < optimizer.jacobian[i].length; j++) {
                assertEquals(0.0, optimizer.jacobian[i][j], 1e-8);
            }
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunctionThrowingException();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{0.0});
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{0.0});
        optimizer.updateResidualsAndCost();
        assertEquals(0, optimizer.getCost(), 1e-8);
        assertEquals(0, optimizer.getEvaluations(), 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunctionThrowingException();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{0.0});
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}, new double[]{0.0, 0.0, 0.0});
        optimizer.updateResidualsAndCost();
        assertEquals(Math.sqrt(3.0) / Math.sqrt(3.0), optimizer.getRMS(), 1e-8);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}, new double[]{0.0, 0.0, 0.0});
        optimizer.updateResidualsAndCost();
        assertEquals(3.0, optimizer.getChiSquare(), 1e-8);
    }

    @Test
    public void testGetCovariances() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}, new double[]{0.0, 0.0, 0.0});
        optimizer.updateJacobian();
        double[][] covariances = optimizer.getCovariances();
        assertEquals(optimizer.getCols(), covariances.length);
        assertEquals(optimizer.getCols(), covariances[0].length);
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                assertEquals(0.0, covariances[i][j], 1e-8);
            }
        }
        optimizer.updateResidualsAndCost();
        covariances = optimizer.getCovariances();
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                assertEquals(0.0, covariances[i][j], 1e-8);
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorFunction function = mockFunction();
        optimizer.optimize(function, new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}, new double[]{0.0, 0.0, 0.0});
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(optimizer.getCols(), errors.length);
        for (int i = 0; i < errors.length; i++) {
            assertEquals(0.0, errors[i], 1e-8);
        }
    }

    private DifferentiableMultivariateVectorFunction mockFunction() throws Exception {
        return new DifferentiableMultivariateVectorFunction() {
            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                double[] result = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    result[i] = Math.pow(i, 2);
                }
                return result;
            }
        };
    }

    private DifferentiableMultivariateVectorFunction mockFunctionThrowingException() throws Exception {
        return new DifferentiableMultivariateVectorFunction() {
            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(new Exception("Mock exception"));
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                return new double[x.length];
            }
        };
    }

    @Override
    protected PointValuePair doOptimizeTest(DifferentiableMultivariateVectorFunction function, double[] startPoint, double[] target, double[] bounds, int maxIterations, double tolerance) throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(maxIterations);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(tolerance));
        return optimizer.optimize(function, target, null, startPoint);
    }

    @Override
    protected void setUp() {
        super.setUp();
        junit.framework.Assert.assertEquals("org.apache.commons.math3.optim.PointValuePair", PointValuePair.class.getName());
    }

    @Override
    protected DoubleFunction mockFunction() {
        return new DoubleFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
    }

    @Override
    protected VectorFunction mockVectorFunction() {
        return new VectorFunction() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                double[] result = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    result[i] = Math.pow(x[i], 2);
                }
                return result;
            }

            @Override
            public JacobianMatrix gradient(double[] x) throws FunctionEvaluationException {
                return new JacobianMatrix(new double[][]{new double[]{2 * x[0]}});
            }
        };
    }

    @Override
    protected Bounds getBounds() {
        return new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{10.0, -10.0});
    }

    @Override
    protected InitialGuess getInitialGuess() {
        return new InitialGuess(new double[]{0.0});
    }

    @Override
    protected MaxEval getMaxEvaluations() {
        return new MaxEval(100);
    }

}