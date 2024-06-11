import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    // initialize the GaussNewtonOptimizer
    GaussNewtonOptimizer optimizer;

    @Test
    public void testDoOptimize() {
        // create an instance of the optimizer
        optimizer = new GaussNewtonOptimizer();

        // set the parameters for the optimizer
        optimizer.setTarget(new double[]{1.0, 2.0, 3.0});
        optimizer.setWeight(new ArrayRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}));
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});

        // optimize the function
        PointVectorValuePair result = optimizer.doOptimize();

        // check the result
        assertEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 0.000001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        // create an instance of the optimizer
        optimizer = new GaussNewtonOptimizer();

        // set the parameters for the optimizer
        optimizer.setTarget(new double[]{1.0, 2.0, 3.0});
        optimizer.setWeight(new ArrayRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}));
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});

        // set a null checker
        optimizer.setConvergenceChecker(null);

        // optimize the function
        optimizer.doOptimize();
    }
}