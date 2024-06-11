import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
         optimizer = new AbstractLeastSquaresOptimizer() {
             protected VectorialPointValuePair doOptimize() {
                 // Do nothing for testing purposes
                 return null;
             }
         };
    }

    @Test
    public void testSetMaxIterations() {
        int maxIterations = 10;
        optimizer.setMaxIterations(maxIterations);
        Assert.assertEquals(maxIterations, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        int iterations = 5;
        optimizer.setIterations(iterations);
        Assert.assertEquals(iterations, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        optimizer.setEvaluations(evaluations);
        Assert.assertEquals(evaluations, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        int jacobianEvaluations = 20;
        optimizer.setJacobianEvaluations(jacobianEvaluations);
        Assert.assertEquals(jacobianEvaluations, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        VectorialConvergenceChecker convergenceChecker = mock(VectorialConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetRMS() {
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);
        double expected = Math.sqrt(1 * 1 + 2 * 2 + 3 * 3) / 3;
        Assert.assertEquals(expected, optimizer.getRMS(), 0.00001);
    }

    @Test
    public void testGetChiSquare() {
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);
        double expected = 1 * 1 + 2 * 2 + 3 * 3;
        Assert.assertEquals(expected, optimizer.getChiSquare(), 0.00001);
    }

    @Test
    public void testGetCovariances() {
        // Mock the jacobian matrix
        double[][] jacobian = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MultivariateMatrixFunction jacobianFunction = mock(MultivariateMatrixFunction.class);
        when(jacobianFunction.value(optimizer.getPoint())).thenReturn(jacobian);
        optimizer.setJacobian(jacobianFunction);

        // Call the getCovariances method
        try {
            double[][] covariances = optimizer.getCovariances();
            Assert.assertArrayEquals(jacobian, covariances);
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        } catch (OptimizationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGuessParametersErrors() {
        // Mock the jacobian matrix
        double[][] jacobian = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MultivariateMatrixFunction jacobianFunction = mock(MultivariateMatrixFunction.class);
        when(jacobianFunction.value(optimizer.getPoint())).thenReturn(jacobian);
        optimizer.setJacobian(jacobianFunction);

        // Call the guessParametersErrors method
        try {
            double[] errors = optimizer.guessParametersErrors();
            Assert.assertArrayEquals(jacobian[0], errors, 0.00001);
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        } catch (OptimizationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOptimize() {
        // Mock the function, target and weights
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};

        // Call the optimize method
        try {
            VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
            Assert.assertNull(result);
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        } catch (OptimizationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}