import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        double[] targetValues = {1, 2, 3, 4, 5}; // typical use case

        double[][] weightData = {{2, 0, 0, 0, 0},
                                 {0, 2, 0, 0, 0},
                                 {0, 0, 2, 0, 0},
                                 {0, 0, 0, 2, 0},
                                 {0, 0, 0, 0, 2}}; // typical use case
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightData);

        double[] startPoint = {0, 0, 0, 0, 0}; // typical use case

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();

        double[] expectedPoint = {1, 2, 3, 4, 5}; // typical use case
        double[] actualPoint = result.getPoint();

        assertArrayEquals(expectedPoint, actualPoint, 1e-6);

        double[] expectedValues = targetValues;
        double[] actualValues = result.getValue();

        assertArrayEquals(expectedValues, actualValues, 1e-6);
    }

    @Test
    public void testDoOptimizeWithEmptyTarget() {
        double[] targetValues = {}; // edge case

        double[][] weightData = {}; // edge case
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightData);

        double[] startPoint = {}; // edge case

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();

        double[] expectedPoint = {}; // edge case
        double[] actualPoint = result.getPoint();

        assertArrayEquals(expectedPoint, actualPoint, 1e-6);

        double[] expectedValues = targetValues;
        double[] actualValues = result.getValue();

        assertArrayEquals(expectedValues, actualValues, 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        double[] targetValues = {1, 2, 3, 4, 5}; // typical use case

        double[][] weightData = {{2, 0, 0, 0, 0},
                                 {0, 2, 0, 0, 0},
                                 {0, 0, 2, 0, 0},
                                 {0, 0, 0, 2, 0},
                                 {0, 0, 0, 0, 2}}; // typical use case
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightData);

        double[] startPoint = {0, 0, 0, 0, 0}; // typical use case

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize(); // This should throw NullArgumentException
    }
}