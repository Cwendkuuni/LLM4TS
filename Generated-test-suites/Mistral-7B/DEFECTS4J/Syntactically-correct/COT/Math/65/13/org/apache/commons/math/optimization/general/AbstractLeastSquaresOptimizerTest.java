import org.apache.commons.math3.optim.AbstractOptimizer;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.exception.OptimizationException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxEvaluations(5);
        assertEquals(10, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        AbstractOptimizer optimizerSpy = createOptimizerSpy();
        optimizer.setConvergenceChecker(optimizerSpy);
        optimizer.setMaxIterations(1);

        optimizer.incrementIterationsCounter();
        assertTrue(optimizer.getIterations() == 1);
        assertTrue(optimizerSpy.isConverged());

        optimizer.incrementIterationsCounter();
        assertTrue(optimizer.getIterations() == 2);
        assertTrue(optimizerSpy.isConverged());

        optimizer.incrementIterationsCounter();
        assertTrue(optimizer.getIterations() == 3);
        assertFalse(optimizerSpy.isConverged());
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);

        optimizer.updateJacobian();
        assertEquals(optimizer.getRows(), optimizer.jacobian.length);
        assertEquals(optimizer.getCols(), optimizer.jacobian[0].length);
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        optimizer.updateResidualsAndCost();
        PointValuePair result = optimizer.doOptimize();
        assertEquals(result.getValue(), new double[] {1.5707963267948966, 0.7853981633974483}, result.getPoint());
        assertEquals(Math.sqrt(2.0), optimizer.getCost(), 0.00001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        optimizer.setMaxIterations(0);
        optimizer.setPoint(new double[] {0.0, 0.0});

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, new double[2], new double[2], new double[2]));
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        optimizer.setMaxEvaluations(0);
        optimizer.setPoint(new double[] {0.0, 0.0});

        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.optimize(function, new double[2], new double[2], new double[2]));
    }

    @Test
    public void testGetRMS() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        optimizer.updateResidualsAndCost();
        assertEquals(Math.sqrt(1.5), optimizer.getRMS(), 0.00001);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        optimizer.updateResidualsAndCost();
        assertEquals(3.0, optimizer.getChiSquare(), 0.00001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        double[][] covariances = optimizer.getCovariances();
        assertArrayEquals(new double[][] {{1.0, 1.0}, {1.0, 1.0}}, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        optimizer.updateResidualsAndCost();
        double[] errors = optimizer.guessParametersErrors();
        assertArrayEquals(new double[] {Math.sqrt(1.0), Math.sqrt(1.0)}, errors);
    }

    @Test
    public void testDoOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = createOptimizerSpy();
        DifferentiableMultivariateVectorialFunction function = createMockFunction();
        optimizer.setFunction(function);
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setPoint(new double[] {0.0, 0.0});

        PointValuePair result = optimizer.doOptimize();
        assertEquals(new double[] {1.5707963267948966, 0.7853981633974483}, result.getPoint());
        assertEquals(new double[] {1.0001570796326795, 0.00007853981633974483}, result.getValue());
        assertTrue(optimizer.isConverged());
    }

    private DifferentiableMultivariateVectorialFunction createMockFunction() {
        return mock(DifferentiableMultivariateVectorialFunction.class);
    }

    private AbstractOptimizer createOptimizerSpy() {
        return spy(AbstractOptimizer.class);
    }
}