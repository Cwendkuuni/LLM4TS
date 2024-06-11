package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private static final double EPSILON = 1e-12;

    private GaussNewtonOptimizer optimizer;
    private ConvergenceChecker<PointVectorValuePair> checker;
    private RealMatrix weightMatrix;
    private double[] startPoint;
    private double[] targetValues;

    @Before
    public void setUp() throws Exception {
        optimizer = new GaussNewtonOptimizer(true, checker = new SimpleVectorValueChecker());
        weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        startPoint = new double[]{0.0, 0.0, 0.0};
        targetValues = new double[]{1.0, 1.0, 1.0};
    }

    @Test
    public void testSimpleTest() {
        optimizer.optimize(targetValues, weightMatrix, startPoint);
        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        Assert.assertArrayEquals(expectedPoint, optimizer.point, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTargetValues() {
        optimizer.optimize(null, weightMatrix, startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        optimizer.optimize(targetValues, null, startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(targetValues, weightMatrix, null);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularWeightMatrix() throws Exception {
        weightMatrix = new BlockRealMatrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        optimizer.optimize(targetValues, weightMatrix, startPoint);
    }

    @Test
    public void testConvergenceChecker() {
        checker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return iter > 10;
            }
        };
        optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.optimize(targetValues, weightMatrix, startPoint);
        Assert.fail();
    }
}