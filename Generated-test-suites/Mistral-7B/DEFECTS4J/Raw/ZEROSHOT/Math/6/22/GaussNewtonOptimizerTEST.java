import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.function.GaussNewtonFunction;
import org.apache.commons.math3.optim.nonlinear.vector.GaussNewtonVectorFunction;
import org.apache.commons.math3.optim.nonlinear.vector.leastsq.GaussNewtonOptimizer;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private RealMatrix matrix;
    private RealVector vector;
    private RealVector startPoint;
    private double[] targetValues;
    private RealVector target;
    private RealVector result;
    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        int n = 4;
        matrix = new BlockRealMatrix(new int[][]{
                {1, 2, 2, 1},
                {2, 3, 3, 2},
                {2, 3, 4, 3},
                {1, 2, 3, 4}
        });
        vector = new ArrayRealVector(new double[]{1, 2, 2.5, 3.5});
        targetValues = new double[]{1, 2, 3, 5};
        target = new ArrayRealVector(targetValues);
        startPoint = new ArrayRealVector(new double[]{0, 0, 0, 0});
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-6));
    }

    @Test
    public void testGaussNewtonOptimizer() {
        DifferentiableUnivariateFunction function = new GaussNewtonFunction(new GaussNewtonVectorFunction(matrix, vector), target);
        PointValuePair expected = function.optimize();
        result = optimizer.optimize(startPoint.toArray()).getPoint();
        Assert.assertArrayEquals(expected.getValue(), result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        optimizer.optimize(startPoint.toArray());
    }
}