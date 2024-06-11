import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction f;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizerImpl();
        f = new DifferentiableMultivariateVectorialFunctionImpl();
        target = new double[] {1, 2, 3};
        weights = new double[] {1, 1, 1};
        startPoint = new double[] {0, 0, 0};
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
        VectorialPointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        
        double[] expectedPoint = new double[] {1, 2, 3};
        double[] expectedValue = new double[] {0, 0, 0};
        
        assertArrayEquals(expectedPoint, result.getPoint(), 0);
        assertArrayEquals(expectedValue, result.getValue(), 0);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeWithWrongDimensions() throws FunctionEvaluationException, OptimizationException {
        double[] wrongTarget = new double[] {1, 2};
        VectorialPointValuePair result = optimizer.optimize(f, wrongTarget, weights, startPoint);
    }

}