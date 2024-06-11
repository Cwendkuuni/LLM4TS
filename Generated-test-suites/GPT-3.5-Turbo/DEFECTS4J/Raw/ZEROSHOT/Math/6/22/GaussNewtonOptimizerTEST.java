import org.junit.*;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testDoOptimize() {
        // Set up the test data
        double[] targetValues = {1.0, 2.0, 3.0};
        double[][] weightMatrixData = {{1.0, 0.0, 0.0},
                                       {0.0, 1.0, 0.0},
                                       {0.0, 0.0, 1.0}};
        double[] startPoint = {0.0, 0.0, 0.0};

        // Set the test data in the optimizer
        optimizer.setTarget(targetValues);
        optimizer.setWeight(new BlockRealMatrix(weightMatrixData));
        optimizer.setStartPoint(startPoint);

        // Perform the optimization
        PointVectorValuePair result = optimizer.doOptimize();

        // Check the result
        double[] expectedPoint = {1.0, 1.0, 1.0};
        double[] expectedObjective = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.01);
        assertArrayEquals(expectedObjective, result.getValue(), 0.01);
    }
}