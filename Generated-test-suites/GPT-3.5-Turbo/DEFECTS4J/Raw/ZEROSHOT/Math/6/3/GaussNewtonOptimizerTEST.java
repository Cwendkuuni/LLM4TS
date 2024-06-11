import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }
    
    @Test
    public void testDoOptimize() {
        double[] targetValues = {1, 2, 3};
        RealMatrix weightMatrix = MatrixUtils.createRealIdentityMatrix(targetValues.length);
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(new double[] {0, 0, 0});
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        double[] expectedPoint = {0, 1, 2};
        double[] expectedValue = {1, 4, 9};
        
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertArrayEquals(expectedValue, result.getValue(), 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularProblem() {
        double[] targetValues = {1, 2, 3};
        RealMatrix weightMatrix = MatrixUtils.createRealIdentityMatrix(targetValues.length);
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(new double[] {0, 0, 0});
        optimizer.doOptimize();
    }
    
    @Test
    public void testDoOptimizeConverged() {
        double[] targetValues = {1, 2, 3};
        RealMatrix weightMatrix = MatrixUtils.createRealIdentityMatrix(targetValues.length);
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(new double[] {0, 0, 0});
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        double[] expectedPoint = {0, 1, 2};
        double[] expectedValue = {1, 4, 9};
        
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertArrayEquals(expectedValue, result.getValue(), 0.0001);
    }
}