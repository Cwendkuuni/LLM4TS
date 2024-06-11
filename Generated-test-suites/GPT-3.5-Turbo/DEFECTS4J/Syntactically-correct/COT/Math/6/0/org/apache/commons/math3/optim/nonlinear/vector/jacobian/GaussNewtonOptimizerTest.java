
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        optimizer.setTarget(new double[]{3.0, 4.0});
        optimizer.setWeight(new ArrayRealVector(new double[]{1.0, 1.0}).toArray());
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        double[] expectedPoint = new double[]{2.0, 2.0};
        double[] expectedValue = new double[]{3.0, 4.0};
        
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        Assert.assertArrayEquals(expectedValue, result.getValue(), 0.0001);
    }

    // Test computeObjectiveValue method
    @Test
    public void testComputeObjectiveValue() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        double[] result = optimizer.computeObjectiveValue(new double[]{3.0, 4.0});
        
        double[] expected = new double[]{3.0, 4.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test computeResiduals method
    @Test
    public void testComputeResiduals() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        double[] result = optimizer.computeResiduals(new double[]{3.0, 4.0});
        
        double[] expected = new double[]{2.0, 2.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test computeWeightedJacobian method
    @Test
    public void testComputeWeightedJacobian() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        RealMatrix result = optimizer.computeWeightedJacobian(new double[]{3.0, 4.0});
        
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        
        Assert.assertEquals(expected, result);
    }

    // Test getConvergenceChecker method
    @Test
    public void testGetConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        
        ConvergenceChecker<PointVectorValuePair> result = optimizer.getConvergenceChecker();
        
        Assert.assertNotNull(result);
    }

    // Test setConvergenceChecker method
    @Test
    public void testSetConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker();
        
        optimizer.setConvergenceChecker(convergenceChecker);
        
        ConvergenceChecker<PointVectorValuePair> result = optimizer.getConvergenceChecker();
        
        Assert.assertEquals(convergenceChecker, result);
    }

    // Test getStartPoint method
    @Test
    public void testGetStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        double[] result = optimizer.getStartPoint();
        
        double[] expected = new double[]{1.0, 2.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test setStartPoint method
    @Test
    public void testSetStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        double[] result = optimizer.getStartPoint();
        
        double[] expected = new double[]{1.0, 2.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test getTarget method
    @Test
    public void testGetTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setTarget(new double[]{3.0, 4.0});
        
        double[] result = optimizer.getTarget();
        
        double[] expected = new double[]{3.0, 4.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test setTarget method
    @Test
    public void testSetTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setTarget(new double[]{3.0, 4.0});
        
        double[] result = optimizer.getTarget();
        
        double[] expected = new double[]{3.0, 4.0};
        
        Assert.assertArrayEquals(expected, result, 0.0001);
    }

    // Test getWeight method
    @Test
    public void testGetWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setWeight(new ArrayRealVector(new double[]{1.0, 1.0}).toArray());
        
        RealMatrix result = optimizer.getWeight();
        
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        
        Assert.assertEquals(expected, result);
    }

    // Test setWeight method
    @Test
    public void testSetWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setWeight(new ArrayRealVector(new double[]{1.0, 1.0}).toArray());
        
        RealMatrix result = optimizer.getWeight();
        
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        
        Assert.assertEquals(expected, result);
    }

    // Test computeCost method
    @Test
    public void testComputeCost() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        
        double[] residuals = new double[]{1.0, 2.0};
        
        double result = optimizer.computeCost(residuals);
        
        double expected = 5.0;
        
        Assert.assertEquals(expected, result, 0.0001);
    }

    // Test point field after optimization
    @Test
    public void testPointField() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        optimizer.setTarget(new double[]{3.0, 4.0});
        optimizer.setWeight(new ArrayRealVector(new double[]{1.0, 1.0}).toArray());
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        double[] expected = new double[]{2.0, 2.0};
        
        Assert.assertArrayEquals(expected, optimizer.point, 0.0001);
    }
}
