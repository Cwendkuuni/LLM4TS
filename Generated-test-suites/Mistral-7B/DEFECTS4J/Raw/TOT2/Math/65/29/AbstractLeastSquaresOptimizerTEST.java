import org.apache.commons.math3.optim.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorFunction;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testOptimizerDefaultConstructor() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testOptimizerWithFunction() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorFunction(new double[]{-1d, 2d}, x -> Math.pow(x[0], 2) + Math.pow(x[1], 2));
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        PointValuePair result = optimizer.optimize(function, new double[]{4, 4}, null, new double[]{2, 2});
        Assert.assertArrayEquals(new double[]{0, 0}, result.getPoint(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizerMaxIterationsExceeded() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorFunction(new double[]{0d}, x -> Math.pow(x[0], 2));
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, new double[]{1}, null, new double[]{1});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizerMaxEvaluationsExceeded() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorFunction(new double[]{0d}, x -> Math.pow(x[0], 2));
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, new double[]{1}, null, new double[]{1});
    }
}