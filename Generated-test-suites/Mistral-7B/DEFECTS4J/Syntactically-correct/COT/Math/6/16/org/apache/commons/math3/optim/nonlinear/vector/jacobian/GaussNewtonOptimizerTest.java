import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.optimization.point.PointValuePair;
import org.apache.commons.math3.optimization.simple.SimpleVectorValueChecker;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        optimizer.setConvergenceChecker(checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
    }

    @Test
    public void testGaussNewtonOptimizerSimpleConstructor() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertFalse(optimizer.isUseLU());
        optimizer.setUseLU(true);
        optimizer.setConvergenceChecker(checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        optimizer.optimize(problem);
    }

    @Test
    public void testGaussNewtonOptimizerUseLUConstructor() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertTrue(optimizer.isUseLU());
        optimizer.setConvergenceChecker(checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizerInvalidUseLU() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setUseLU(-1);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        optimizer.optimize(problem);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerNoConvergence() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        checker.setMaxIterations(1);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{}, new double[]{}, new double[]{}, null);
        optimizer.optimize(problem);
    }

    @Test
    public void testGaussNewtonOptimizerConvergence() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        checker.setMaxIterations(1000);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{1.0, 1.0}, new double[]{2.0, 3.0}, new double[]{1.1, 1.2}, null);
        optimizer.optimize(problem);
        PointValuePair result = optimizer.getPoint();
        RealVector expected = new ArrayRealVector(new double[]{1.0 - 0.1, 1.0 - 0.1});
        assertEquals(expected, result.getVector());
    }

    @Test(expected = SingularMatrixException.class)
    public void testGaussNewtonOptimizerSingularMatrixException() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        LeastSquaresProblem problem = new LeastSquaresProblem(new double[]{1.0, 1.0}, new double[]{2.0, 3.0}, new double[]{}, new double[]{1.1, 1.2});
        optimizer.optimize(problem);
    }
}