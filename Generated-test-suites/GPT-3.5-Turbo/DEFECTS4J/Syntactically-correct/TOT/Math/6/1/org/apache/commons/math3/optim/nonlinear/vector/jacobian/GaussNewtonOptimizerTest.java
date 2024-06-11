import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1: Typical Use Case
        double[] startPoint = {1, 2, 3};
        double[] target = {4, 5, 6};
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertArrayEquals(target, result.getSecond(), 0.001);

        // Test case 2: Edge Case
        startPoint = new double[]{0, 0, 0};
        target = new double[]{0, 0, 0};
        weight = new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        result = optimizer.doOptimize();
        Assert.assertArrayEquals(target, result.getSecond(), 0.001);

        // Test case 3: Error Scenario
        startPoint = new double[]{1, 2, 3};
        target = new double[]{4, 5};
        weight = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        try {
            result = optimizer.doOptimize();
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Exception expected
        }
    }
}