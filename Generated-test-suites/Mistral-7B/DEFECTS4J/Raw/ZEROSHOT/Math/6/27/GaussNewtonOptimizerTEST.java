import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.LUDecompositionSolver;
import org.apache.commons.math3.optim.nonlinear.linear.QRDecompositionSolver;
import org.apache.commons.math3.optim.points.RealVectorValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealVectorValue problem;

    @Before
    public void setUp() {
        double[] x = {1.0, 2.0};
        double[] f = {3.0, 4.0};
        double[] g = {2.5, 3.5};

        optimizer = new GaussNewtonOptimizer(new LUDecompositionSolver(), new SimpleVectorValueChecker(1e-8, 1e-8));
        problem = new UnconstrainedOptimizationProblem(new RealVectorValue(x), f, g, new AveragedStartingPointGenerator(x));
    }

    @Test
    public void testGaussNewtonOptimizer() {
        PointValuePair result = optimizer.optimize(problem);
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{1.0, 1.5}, result.getPoint(), 1e-8);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        optimizer = new GaussNewtonOptimizer(null);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        optimizer = new GaussNewtonOptimizer(new QRDecompositionSolver(), null);
        optimizer.optimize(problem);
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        optimizer = new GaussNewtonOptimizer(new QRDecompositionSolver(), new SimpleVectorValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(problem);
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{1.0, 1.5}, result.getPoint(), 1e-8);
    }

    @Test
    public void testLevenbergMarquardtOptimizer() {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(1e-8, 1e-8);
        PointValuePair result = levenbergMarquardtOptimizer.optimize(problem);
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{1.0, 1.5}, result.getPoint(), 1e-8);
    }
}