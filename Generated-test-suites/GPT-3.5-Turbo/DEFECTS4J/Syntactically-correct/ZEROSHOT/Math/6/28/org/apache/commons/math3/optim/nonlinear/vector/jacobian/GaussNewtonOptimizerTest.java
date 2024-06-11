import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(target);

        // Set the weight matrix
        double[][] weightData = {{1.0, 0.0, 0.0},
                                 {0.0, 1.0, 0.0},
                                 {0.0, 0.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        optimizer.setWeight(weightMatrix);

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Optimize and get the result
        PointVectorValuePair result = optimizer.doOptimize();

        // Get the optimized point and check its values
        double[] optimizedPoint = result.getPoint();
        assertEquals(1.0, optimizedPoint[0], 1e-15);
        assertEquals(2.0, optimizedPoint[1], 1e-15);
        assertEquals(3.0, optimizedPoint[2], 1e-15);

        // Get the optimized value and check its value
        double optimizedValue = result.getValue();
        assertEquals(0.0, optimizedValue, 1e-15);
    }
}