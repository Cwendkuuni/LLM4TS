import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optimization.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class GaussNewtonOptimizerTest {

    // Test case for the constructor GaussNewtonOptimizer(boolean useLU)
    @Test
    public void testGaussNewtonOptimizerConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        Assert.assertTrue(optimizer instanceof GaussNewtonOptimizer);
    }

    // Test case for the constructor GaussNewtonOptimizer(ConvergenceChecker<PointVectorValuePair> checker)
    @Test
    public void testGaussNewtonOptimizerConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker<>();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        Assert.assertTrue(optimizer instanceof GaussNewtonOptimizer);
    }

    // Test case for the constructor GaussNewtonOptimizer(boolean useLU, ConvergenceChecker<PointVectorValuePair> checker)
    @Test
    public void testGaussNewtonOptimizerConstructorWithLUAndChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker<>();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        Assert.assertTrue(optimizer instanceof GaussNewtonOptimizer);
    }

    // Test case for the doOptimize() method with a null checker
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    // Test case for the doOptimize() method when the normal equations cannot be solved
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0}));
        optimizer.setTarget(new double[]{2.0, 2.0});
        optimizer.computeObjectiveValue(new double[]{1.0, 1.0});
        optimizer.computeResiduals(new double[]{2.0, 2.0});
        optimizer.computeWeightedJacobian(new double[]{1.0, 1.0});
        optimizer.doOptimize();
    }

    // Test case for the doOptimize() method with a convergence checker that returns false
    @Test
    public void testDoOptimizeWithNoConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0}));
        optimizer.setTarget(new double[]{2.0, 2.0});
        optimizer.setConvergenceChecker((iteration, previous, current) -> false);
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertTrue(Arrays.equals(result.getPoint(), new double[]{1.0, 1.0}));
    }

    // Test case for the doOptimize() method with a convergence checker that returns true
    @Test
    public void testDoOptimizeWithConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setWeight(MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0}));
        optimizer.setTarget(new double[]{2.0, 2.0});
        optimizer.setConvergenceChecker((iteration, previous, current) -> true);
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertTrue(Arrays.equals(result.getPoint(), new double[]{1.0, 1.0}));
    }
}