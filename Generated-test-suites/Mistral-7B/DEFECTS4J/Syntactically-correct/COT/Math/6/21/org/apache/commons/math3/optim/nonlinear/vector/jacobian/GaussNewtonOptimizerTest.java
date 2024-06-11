package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;

/**
 * Test cases for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    /**
     * Test constructor with default settings.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorValueChecker);
    }

    /**
     * Test constructor with custom settings.
     */
    @org.junit.Test
    public void testCustomConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom settings and custom convergence checker.
     */
    @org.junit.Test
    public void testCustomConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertTrue(optimizer.isUseLU());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Test optimize method with default settings.
     * @throws Exception
     */
    @org.junit.Test
    public void testOptimizeWithDefaultSettings() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizationTest(optimizer, new SimpleFunction(), new double[]{0.0}, 0.0);
    }

    /**
     * Test optimize method with custom settings.
     * @throws Exception
     */
    @org.junit.Test
    public void testOptimizeWithCustomSettings() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        optimizationTest(optimizer, new SimpleFunction(), new double[]{0.0}, 0.0);
    }

    /**
     * Test optimize method with custom convergence checker.
     * @throws Exception
     */
    @org.junit.Test
    public void testOptimizeWithCustomChecker() throws Exception {
        ConvergenceChecker<PointVectorValuePair> checker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizationTest(optimizer, new SimpleFunction(), new double[]{0.0}, 0.0);
    }

    /**
     * Test optimize method with non-convergent function (singular matrix).
     * @throws Exception
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testOptimizeWithNonConvergentFunction() throws Exception {
        Function function = new SingularFunction();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizationTest(optimizer, function, new double[]{0.0}, 0.0);
    }

    /**
     * Test optimize method with null input.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullInput() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.setObjectiveFunction(null);
        optimizer.optimize();
    }

    /**
     * Test optimize method with empty input.
     * @throws Exception
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testOptimizeWithEmptyInput() throws Exception {
        Function function = new EmptyFunction();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.setObjectiveFunction(function);
        optimizer.optimize();
    }

    /**
     * Test optimize method with incorrect input size.
     * @throws Exception
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testOptimizeWithIncorrectInputSize() throws Exception {
        Function function = new SimpleFunction();
        RealVector startPoint = new ArrayRealVector(new double[]{0.0, 1.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
    }

    private void optimizationTest(GaussNewtonOptimizer optimizer, Function function, double[] startPoint, double expectedValue)
            throws Exception {
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        PointValuePair result = optimizer.optimize();
        RealVector point = result.getPoint();
        double value = result.getValue();
        assertEquals(expectedValue, value, 1e-12);
        assertArrayEquals(expectedStartPoint, point.toArray(), 1e-12);
    }

    private static double[] expectedStartPoint = new double[]{1.0, 1.0};

    // Custom functions for testing

    /**
     * Function for testing optimization with a singular matrix.
     */
    static class SingularFunction implements Function {
        @Override
        public double value(RealVector x) {
            RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                    {1.0, 0.0}, {0.0, 0.0}
            });
            double[] currentPoint = x.toArray();
            double[] currentObjective = optimizer.computeObjectiveValue(currentPoint);
            double[] currentResiduals = optimizer.computeResiduals(currentObjective);
            RealMatrix weightedJacobian = optimizer.computeWeightedJacobian(currentPoint);
            double[] b = new double[currentPoint.length];
            double[][] a = new double[currentPoint.length][currentPoint.length];
            for (int i = 0; i < currentPoint.length; ++i) {

                double[] grad = weightedJacobian.getRow(i);
                double weight = 1.0;
                double residual = currentResiduals[i];

                for (int j = 0; j < currentPoint.length; ++j) {
                    b[j] += weight * residual * grad[j];
                }

                for (int k = 0; k < currentPoint.length; ++k) {
                    double[] ak = a[k];
                    double wgk = weight * grad[k];
                    for (int l = 0; l < currentPoint.length; ++l) {
                        ak[l] += wgk * grad[l];
                    }
                }
            }

            RealMatrix mA = new BlockRealMatrix(a);
            DecompositionSolver solver = optimizer.useLU ?
                    new LUDecomposition(mA).getSolver() :
                    new QRDecomposition(mA).getSolver();
            RealVector dX = solver.solve(new ArrayRealVector(b, false));

            for (int i = 0; i < currentPoint.length; ++i) {
                currentPoint[i] += dX.getEntry(i);
            }

            return 0.0;
        }
    }

    /**
     * Empty function for testing optimization with empty input.
     */
    static class EmptyFunction implements Function {
        @Override
        public double value(RealVector x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RealVector gradient(RealVector x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}