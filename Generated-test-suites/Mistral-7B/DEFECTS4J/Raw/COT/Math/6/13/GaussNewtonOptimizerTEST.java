import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.optimization.point.PointValuePair;
import org.apache.commons.math3.optimization.convergence.SimpleVectorValueChecker;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructorWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertTrue(optimizer.isUseLU());
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testGaussNewtonOptimizerConstructorWithSimpleVectorValueChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        assertFalse(optimizer.isUseLU());
        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        assertEquals(1e-6, checker.getAbsoluteTolerance(), 0.0);
        assertEquals(1e-3, checker.getRelativeTolerance(), 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerConstructorWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertTrue(optimizer.isUseLU());
        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        assertEquals(1e-6, checker.getAbsoluteTolerance(), 0.0);
        assertEquals(1e-3, checker.getRelativeTolerance(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerConstructorWithNullConvergenceChecker() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDoOptimize_DefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        LeastSquaresProblem problem = new LeastSquaresProblem(1, new ArrayRealVector(new double[]{1, 2, 3}));
        optimizer.setObjectiveFunction(problem.getValue());
        optimizer.setGradient(problem.getGradient());
        optimizer.setStartPoint(new ArrayRealVector(new double[]{0.1, 0.2, 0.3}));

        optimizer.optimize();

        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        double[] result = optimizer.getPoint();

        double[] expected = new double[]{1.006628265766478, 1.996851069117867, 2.987073872489256};
        assertArrayEquals(expected, result, 0.001);
        assertEquals(2.546834453180224E-6, checker.lastRelativeError(), 0.0);
    }

    @Test
    public void testDoOptimize_LUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LeastSquaresProblem problem = new LeastSquaresProblem(1, new ArrayRealVector(new double[]{1, 2, 3}));
        optimizer.setObjectiveFunction(problem.getValue());
        optimizer.setGradient(problem.getGradient());
        optimizer.setStartPoint(new ArrayRealVector(new double[]{0.1, 0.2, 0.3}));

        optimizer.optimize();

        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        double[] result = optimizer.getPoint();

        double[] expected = new double[]{1.006628265766478, 1.996851069117867, 2.987073872489256};
        assertArrayEquals(expected, result, 0.001);
        assertEquals(2.546834453180224E-6, checker.lastRelativeError(), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_SingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());

        LeastSquaresProblem problem = new LeastSquaresProblem(1, new ArrayRealVector(new double[]{1, 2, 0}));
        optimizer.setObjectiveFunction(problem.getValue());
        optimizer.setGradient(problem.getGradient());
        optimizer.setStartPoint(new ArrayRealVector(new double[]{0.1, 0.2, 0.3}));

        optimizer.optimize();
    }
}