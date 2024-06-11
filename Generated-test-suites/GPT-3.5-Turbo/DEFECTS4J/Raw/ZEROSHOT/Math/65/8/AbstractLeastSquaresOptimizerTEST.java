import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.exception.LocalizedFormats;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction function;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() throws Exception {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) throws FunctionEvaluationException {
                return null;
            }

            @Override
            public MultivariateMatrixFunction jacobian() {
                return null;
            }
        };
        target = new double[10];
        weights = new double[10];
        startPoint = new double[10];
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected=OptimizationException.class)
    public void testIncrementIterationsCounter() throws OptimizationException {
        optimizer.setMaxIterations(10);
        for(int i=0; i<15; i++){
            optimizer.incrementIterationsCounter();
        }
    }

    @Test
    public void testGetRMS() {
        assertEquals(0, optimizer.getRMS(), 0.0001);
    }

    @Test
    public void testGetChiSquare() {
        assertEquals(0, optimizer.getChiSquare(), 0.0001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        double[][] covariances = optimizer.getCovariances();
        assertNotNull(covariances);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] errors = optimizer.guessParametersErrors();
        assertNotNull(errors);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        assertNull(result);
    }

}