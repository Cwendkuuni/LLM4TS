import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize_StandardUseCase() {
        // Initialize the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set target values
        double[] target = {1, 2, 3, 4, 5};
        optimizer.setTarget(target);

        // Set weights
        double[][] weightData = {{1, 0, 0, 0, 0},
                                 {0, 1, 0, 0, 0},
                                 {0, 0, 1, 0, 0},
                                 {0, 0, 0, 1, 0},
                                 {0, 0, 0, 0, 1}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        optimizer.setWeight(weightMatrix);

        // Set start point
        double[] startPoint = {0, 0, 0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Optimize
        PointVectorValuePair result = optimizer.doOptimize();

        // Verify result
        double[] expectedPoint = {1, 2, 3, 4, 5};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        double[] expectedObjective = {1, 2, 3, 4, 5};
        assertArrayEquals(expectedObjective, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize_InvalidTarget() {
        // Initialize the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set invalid target values (null)
        double[] target = null;
        optimizer.setTarget(target);

        // Set weights
        double[][] weightData = {{1, 0, 0},
                                 {0, 1, 0},
                                 {0, 0, 1}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        optimizer.setWeight(weightMatrix);

        // Set start point
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Optimize and expect exception
        assertThrows(NullArgumentException.class, () -> optimizer.doOptimize());
    }

    @Test
    public void testDoOptimize_ErrorScenario() {
        // Initialize the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set target values
        double[] target = {1, 2, 3};
        optimizer.setTarget(target);

        // Set weights
        double[][] weightData = {{1, 0, 0},
                                 {0, 1, 0},
                                 {0, 0, 1}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        optimizer.setWeight(weightMatrix);

        // Set start point
        double[] startPoint = {0, 0};
        optimizer.setStartPoint(startPoint);

        // Optimize and expect exception
        assertThrows(MathInternalError.class, () -> optimizer.doOptimize());
    }
}