import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        // Test computeObjectiveValue method
        double[] startPoint = {1, 2, 3};
        double[] objective = optimizer.computeObjectiveValue(startPoint);
        Assert.assertArrayEquals(new double[]{0, 0, 0}, objective, 1e-9);
        
        // Test computeResiduals method
        double[] residuals = optimizer.computeResiduals(objective);
        Assert.assertArrayEquals(new double[]{0, 0, 0}, residuals, 1e-9);

        // Test computeWeightedJacobian method
        double[][] weightedJacobian = optimizer.computeWeightedJacobian(startPoint).getData();
        Assert.assertArrayEquals(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, weightedJacobian, 1e-9);
        
        // Test getConvergenceChecker method
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(checker);
        Assert.assertTrue(checker instanceof SimpleVectorValueChecker);
        
        // Test getStartPoint method
        double[] start = optimizer.getStartPoint();
        Assert.assertArrayEquals(new double[]{0, 0, 0}, start, 1e-9);
        
        // Test getTarget method
        double[] target = optimizer.getTarget();
        Assert.assertNotNull(target);
        Assert.assertEquals(0, target.length);
        
        // Test getWeight method
        Array2DRowRealMatrix weight = (Array2DRowRealMatrix) optimizer.getWeight();
        Assert.assertNotNull(weight);
        Assert.assertEquals(0, weight.getRowDimension());
        Assert.assertEquals(0, weight.getColumnDimension());
        
        // Test getCost method
        double cost = optimizer.getCost();
        Assert.assertEquals(0, cost, 1e-9);
        
        // Test doOptimize method

        // Test case when convergence checker is null
        try {
            optimizer.setConvergenceChecker(null);
            optimizer.doOptimize();
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        // Test case when solver fails to solve linearized least squares problem
        GaussNewtonOptimizer testOptimizer = new GaussNewtonOptimizer(true) {
            @Override
            public PointVectorValuePair doOptimize() {
                throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM);
            }
        };
        try {
            testOptimizer.doOptimize();
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // Test case when previous is null
        // Since there is no previous value, it should return false
        PointVectorValuePair current = new PointVectorValuePair(new double[]{1,2,3}, new double[]{4,5,6});
        Assert.assertFalse(checker.converged(1, null, current));

    }

    @Test
    public void testGaussNewtonOptimizerWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);

        // Test getConvergenceChecker method
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerWithUseLU() {

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);

        // Test useLU field
        Assert.assertFalse(optimizer.useLU);

        // Test doOptimize method

        // Test case when previous is null
        // Since there is no previous value, it should return false
        PointVectorValuePair current = new PointVectorValuePair(new double[]{1,2,3}, new double[]{4,5,6});
        Assert.assertFalse(optimizer.getConvergenceChecker().converged(1, null, current));
    }

    @Test
    public void testGaussNewtonOptimizerWithUseLUFalse() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        // Test useLU field
        Assert.assertFalse(optimizer.useLU);
    }

}