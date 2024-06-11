import static org.junit.Assert.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.GeneralOptimizationException;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

	private static final double EPSILON = 1e-6;
    private static final double[] TARGET_VALUES = {1, 2, 3};
    private static final double[] RESIDUALS_WEIGHTS = {1, 1, 1};
    private static final double[] START_POINT = {0, 0, 0};
    
    private AbstractLeastSquaresOptimizer optimizer;
    
    private DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
		
		@Override
		public double[] value(double[] point) throws FunctionEvaluationException {
			return new double[] {point[0], point[1], point[2]};
		}
		
		@Override
		public MultivariateMatrixFunction jacobian() {
			return new MultivariateMatrixFunction() {
				
				@Override
				public double[][] value(double[] point) throws FunctionEvaluationException {
					return new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
				}
			};
		}
	};
    
    @Before
    public void setup() {
    	optimizer = new AbstractLeastSquaresOptimizer() {
			
			@Override
			protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException,
					OptimizationException, IllegalArgumentException {
				double[] result = function.value(START_POINT);
				return new VectorialPointValuePair(result, result);
			}
		};
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
    	VectorialPointValuePair result = optimizer.optimize(function, TARGET_VALUES, RESIDUALS_WEIGHTS, START_POINT);
    	double[] expected = {1, 2, 3};
    	assertArrayEquals(expected, result.getPoint(), EPSILON);
    	assertArrayEquals(expected, result.getValue(), EPSILON);
    }
    
    @Test(expected=MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() throws FunctionEvaluationException, OptimizationException {
    	optimizer.setMaxIterations(0);
    	optimizer.optimize(function, TARGET_VALUES, RESIDUALS_WEIGHTS, START_POINT);
    }
    
    @Test(expected=MaxEvaluationsExceededException.class)
    public void testOptimizeMaxEvaluationsExceeded() throws FunctionEvaluationException, OptimizationException {
    	optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, TARGET_VALUES, RESIDUALS_WEIGHTS, START_POINT);
    }

}