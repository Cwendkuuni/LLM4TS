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
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * GaussNewtonOptimizer test cases.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test GaussNewtonOptimizer with correct settings and easy data.
     */
    @Test
    public void testEasyData() {
        // Initialize GaussNewtonOptimizer.
        RealMatrix weight = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        // Define function to be optimized.
        FunctionOneVar f = new FunctionOneVar();

        // Set up initial point.
        double x0 = 1.0;
        double[] x = {x0};

        // Run optimization.
        PointVectorValuePair result = optimizer.optimize(new RealFunction(f), x);

        // Check result.
        Assert.assertEquals(2.0, result.getValue()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
    }

    /**
     * Test GaussNewtonOptimizer with incorrect settings and easy data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullChecker() {
        RealMatrix weight = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        RealFunction f = new FunctionOneVar();
        double[] x = {1.0};
        optimizer.optimize(f, x);
    }

    /**
     * Test GaussNewtonOptimizer with incorrect settings and difficult data.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix weight = new BlockRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        RealFunction f = new FunctionOneVar();
        double[] x = {1.0};
        optimizer.optimize(f, x);
    }

    /**
     * Test GaussNewtonOptimizer with correct settings and difficult data.
     */
    @Test
    public void testDifficultData() {
        RealMatrix weight = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        // Define function to be optimized.
        FunctionOneVar f = new FunctionOneVarDifficult();

        // Set up initial point.
        double x0 = 1.0;
        double[] x = {x0};

        // Run optimization.
        PointVectorValuePair result = optimizer.optimize(new RealFunction(f), x);

        // Check result.
        Assert.assertEquals(Math.PI, result.getValue()[0], 1e-6);
        Assert.assertEquals(-Math.PI / 2.0, result.getPoint()[0], 1e-6);
    }

    /**
     * Test GaussNewtonOptimizer with incorrect settings and difficult data.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceFailure() {
        RealMatrix weight = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0e-12);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        // Define function to be optimized.
        FunctionOneVar f = new FunctionOneVarDifficult();

        // Set up initial point.
        double x0 = 1.0;
        double[] x = {x0};

        // Run optimization.
        PointVectorValuePair result = optimizer.optimize(new RealFunction(f), x);

        // The optimization should fail to converge.
    }
}