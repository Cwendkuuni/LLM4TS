import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerEmptyConstructor() {
        assertThrows(NullArgumentException.class, () -> new GaussNewtonOptimizer());
    }

    @Test
    public void testGaussNewtonOptimizerSimpleConstructorWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(GaussNewtonOptimizer.class, optimizer.getClass());
        assertNull(optimizer.getConvergenceChecker());
        assertTrue(optimizer.isUseLU());
    }

    @Test
    public void testGaussNewtonOptimizerSimpleConstructorWithChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertEquals(GaussNewtonOptimizer.class, optimizer.getClass());
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertTrue(optimizer.isUseLU());
    }

    @Test
    public void testGaussNewtonOptimizerComplexConstructor() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertEquals(GaussNewtonOptimizer.class, optimizer.getClass());
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertTrue(optimizer.isUseLU());
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullCheck() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDoOptimizeEmptyData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {};
        double[] targetValues = {};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        UnconstrainedOptimizationTest optimizationTest = new UnconstrainedOptimizationTest() {
            @Override
            public PointValuePair value(RealVector x) throws Exception {
                return optimizer.optimize(x, targetValues, weightMatrix).getPointValuePair();
            }
        };
        optimizationTest.assertIterations(0);
        optimizationTest.assertConverged();
    }

    @Test
    public void testDoOptimizeSimpleData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        UnconstrainedOptimizationTest optimizationTest = new UnconstrainedOptimizationTest() {
            @Override
            public PointValuePair value(RealVector x) throws Exception {
                return optimizer.optimize(x, targetValues, weightMatrix).getPointValuePair();
            }
        };
        optimizationTest.assertConverged();
        optimizationTest.assertIterations(1);
        double[] expectedResult = {1.5, 2.5};
        assertArrayEquals(expectedResult, optimizationTest.getPoint());
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeUnconvergedData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        UnconstrainedOptimizationTest optimizationTest = new UnconstrainedOptimizationTest() {
            @Override
            public PointValuePair value(RealVector x) throws Exception {
                SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6, 1e-6, 1e-6, 1e-6);
                GaussNewtonOptimizer optimizerWithChecker = new GaussNewtonOptimizer(checker);
                return optimizerWithChecker.optimize(x, targetValues, weightMatrix).getPointValuePair();
            }
        };
        optimizationTest.assertIterations(Integer.MAX_VALUE);
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeSingularData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {1.0, 1.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        UnconstrainedOptimizationTest optimizationTest = new UnconstrainedOptimizationTest() {
            @Override
            public PointValuePair value(RealVector x) throws Exception {
                return optimizer.optimize(x, targetValues, weightMatrix).getPointValuePair();
            }
        };
        optimizationTest.assertIterations(0);
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeInternalError() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {1.0, 1.0};
        RealMatrix weightMatrix = new SingularValueDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}}))
                .getSolver()
                .getInverse()
                .toRealMatrix();
        UnconstrainedOptimizationTest optimizationTest = new UnconstrainedOptimizationTest() {
            @Override
            public PointValuePair value(RealVector x) throws Exception {
                return optimizer.optimize(x, targetValues, weightMatrix).getPointValuePair();
            }
        };
        optimizationTest.assertIterations(0);
    }
}