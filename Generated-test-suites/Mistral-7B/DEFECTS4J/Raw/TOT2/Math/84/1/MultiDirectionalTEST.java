import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationException;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.nonlinear.SimpleFunctions;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = {0.0, 1.0, 2.0};
        RealConvergenceChecker checker = new RealConvergenceChecker(1.0e-6, 100);

        // Test with a simple function
        OptimizationException e = Assert.assertThrows(OptimizationException.class, () -> optimizer.optimize(new SimpleFunctions.Sine(), x, checker));
        Assert.assertEquals("Maximal number of evaluations exceeded", e.getMessage());
        PointValuePair solution = optimizer.optimize(new SimpleFunctions.Sine(), x, checker);
        Assert.assertEquals(Math.PI, solution.getPoint()[0], 1e-6);

        // Test with a function that has multiple solutions
        OptimizationException e2 = Assert.assertThrows(OptimizationException.class, () -> optimizer.optimize(new SimpleFunctions.Quartic(), x, checker));
        Assert.assertEquals("Maximal number of evaluations exceeded", e2.getMessage());
        PointValuePair solution2 = optimizer.optimize(new SimpleFunctions.Quartic(), x, checker);
        Assert.assertEquals(0.0, solution2.getPoint()[0], 1e-6);
        Assert.assertEquals(-2.0, solution2.getPoint()[1], 1e-6);

        // Test with an exception
        Assert.assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(new SimpleFunctions.SineOverX(), x, checker));

        // Test with a null checker
        OptimizationException e3 = Assert.assertThrows(OptimizationException.class, () -> optimizer.optimize(new SimpleFunctions.Sine(), null, checker));
        Assert.assertEquals("Input argument cannot be null: x, optimizer is initialized with null checker", e3.getMessage());

        // Test with a wrong number of dimensions
        OptimizationException e4 = Assert.assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new SimpleFunctions.Sine(), new double[]{0.0, 1.0, 2.0, 3.0}, checker));
        Assert.assertEquals("Function does not support the given number of input dimensions: 4", e4.getMessage());
    }
}