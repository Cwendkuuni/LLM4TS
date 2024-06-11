import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.unconstrained.GaussNewtonOptimizer;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorValueChecker);
    }

    @Test
    public void testGaussNewtonOptimizerCheckerConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorValueChecker);
    }

    @Test
    public void testGaussNewtonOptimizerUseLUConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertEquals(true, optimizer.isUseLU());
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorValueChecker);
    }

    @Test
    public void testGaussNewtonOptimizerUseLUAndCheckerConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorValueChecker);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullConstructorChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });

        PointValuePair result = optimizer.optimize(startPoint, targetValues, weightMatrix);

        assertArrayEquals(new double[]{3.0, 3.0}, result.getPoint(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, optimizer.computeResiduals(result.getValue()), 0.001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testGaussNewtonOptimizerSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}
        });

        optimizer.optimize(startPoint, targetValues, weightMatrix);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerConvergenceException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}
        });

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };

        optimizer.setConvergenceChecker(checker);
        optimizer.optimize(startPoint, targetValues, weightMatrix);
    }
}