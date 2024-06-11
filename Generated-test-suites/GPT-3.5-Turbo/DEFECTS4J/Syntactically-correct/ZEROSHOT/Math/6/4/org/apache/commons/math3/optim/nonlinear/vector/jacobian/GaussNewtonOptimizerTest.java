import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set up test data
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        double[][] jacobianData = {{1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0, 3.0}};
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        // Set up expected result
        double[] expectedPoint = {0.333333333333333, 0.666666666666667, 1.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};
        PointVectorValuePair expected = new PointVectorValuePair(expectedPoint, expectedObjective);

        // Set up actual result
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setJacobian(jacobianData);
        optimizer.setWeight(weightData);
        PointVectorValuePair actual = optimizer.doOptimize();

        // Compare actual result with expected result
        assertArrayEquals(expected.getPoint(), actual.getPoint(), 1e-6);
        assertArrayEquals(expected.getValue(), actual.getValue(), 1e-6);
    }
}