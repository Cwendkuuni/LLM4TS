package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;

    @Before
    public void setup() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
    }

    @Test
    public void testOptimizeSuccess() {
        optimizer.setFunction(new Function() {
            @Override
            public double value(double[] point) {
                return 0.5 * point[0] * point[0] + 2.5 * point[1] * point[1];
            }

            @Override
            public double[] gradient(double[] point) {
                return new double[]{
                        2.0 * point[0],
                        5.0 * point[1]
                };
            }
        });

        optimizer.initialize(new double[]{1.0, 1.0}, weightMatrix);

        PointVectorValuePair result = optimizer.optimize();

        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeFailConvergence() {
        optimizer.setFunction(new Function() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] - 2.0, 2.0);
            }

            @Override
            public double[] gradient(double[] point) {
                return new double[]{
                        4.0 * point[0],
                        0.0
                };
            }
        });

        optimizer.initialize(new double[]{0.0, 0.0}, weightMatrix);

        assertThrows(ConvergenceException.class, optimizer::optimize);
    }

    @Test
    public void testOptimizeFailNullFunction() {
        optimizer.initialize(new double[]{1.0, 1.0}, weightMatrix);
        assertThrows(NullArgumentException.class, optimizer::optimize);
    }

    @Test
    public void testOptimizeFailNullWeight() {
        optimizer.setFunction(new Function() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] point) {
                throw new UnsupportedOperationException();
            }
        });
        optimizer.initialize(new double[]{1.0, 1.0}, null);
        assertThrows(NullArgumentException.class, optimizer::optimize);
    }

    @Test
    public void testOptimizeFailNullStart() {
        optimizer.setFunction(new Function() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] point) {
                throw new UnsupportedOperationException();
            }
        });
        optimizer.initialize(null, weightMatrix);
        assertThrows(NullArgumentException.class, optimizer::optimize);
    }

    @Test
    public void testOptimizeFailSingularProblem() {
        optimizer.setFunction(new Function() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] point) {
                throw new UnsupportedOperationException();
            }
        });
        optimizer.initialize(new double[]{1.0, 1.0}, new LUDecomposition(new ArrayRealVector(new double[]{0.0, 0.0}, false)).getMatrix());
        assertThrows(SingularMatrixException.class, optimizer::optimize);
    }

    @FunctionInterface
    private static interface Function {
        double value(double[] point);

        double[] gradient(double[] point);
    }
}