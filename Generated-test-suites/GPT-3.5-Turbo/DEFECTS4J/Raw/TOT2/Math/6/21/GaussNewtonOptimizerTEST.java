import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testSimpleConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        // Test for a basic optimization scenario
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealVector startPoint = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        PointVectorValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testSingularMatrixExceptionHandling() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        // Test for a singular matrix exception handling scenario
        double[] targetValues = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        try {
            optimizer.doOptimize();
            fail("Expected SingularMatrixException");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains("UNABLE_TO_SOLVE_SINGULAR_PROBLEM"));
        }
    }
}