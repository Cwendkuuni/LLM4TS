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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.Gradient;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test the optimizer with a simple function.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testSimpleFunction() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        PointValuePair result = optimizer.optimize(new GaussNewtonFunction(targetValues), weightMatrix, new double[] {0.0, 0.0});
        Assert.assertArrayEquals(new double[] {1.0, 1.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a singular weight matrix.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularWeightMatrix() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {0.0, 1.0},
                {1.0, 0.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new GaussNewtonFunction(targetValues), weightMatrix, new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a null target.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullTarget() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(null, new RealMatrix(2, 2), new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a null starting point.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullStartingPoint() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new GaussNewtonFunction(targetValues), weightMatrix, null);
    }

    /**
     * Test the optimizer with a non-differentiable function.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonDifferentiableFunction() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new NonDifferentiableFunction(targetValues), weightMatrix, new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a function with local minimum.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithLocalMinimum() throws Exception {
        double[] targetValues = {4.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithLocalMinimum(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {1.0, 1.0});
        Assert.assertArrayEquals(new double[] {2.0, 2.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a non-convex function.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvexFunction() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new NonConvexFunction(targetValues), weightMatrix, new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a non-linear function.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testNonLinearFunction() throws Exception {
        double[] targetValues = {Math.PI, Math.PI};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new NonLinearFunction(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {0.0, 0.0});
        Assert.assertArrayEquals(new double[] {Math.PI / 2.0, Math.PI / 2.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a function where the jacobian is not available.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionWithoutJacobian() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new FunctionWithoutJacobian(targetValues), weightMatrix, new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a function with a very large gradient.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithLargeGradient() throws Exception {
        double[] targetValues = {1.0e10, 1.0e10};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithLargeGradient(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {1.0e-10, 1.0e-10});
        Assert.assertArrayEquals(new double[] {1.0e10, 1.0e10}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a function that does not have a minimum.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithoutMinimum() throws Exception {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new FunctionWithoutMinimum(targetValues), weightMatrix, new double[] {0.0, 0.0});
    }

    /**
     * Test the optimizer with a function where all the solutions are equivalent.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithEquivalentSolutions() throws Exception {
        double[] targetValues = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithEquivalentSolutions(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {0.0, 0.0});
        Assert.assertArrayEquals(new double[] {0.0, 0.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a function where the optimal solution is at the
     * boundary of the domain.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithSolutionAtBoundary() throws Exception {
        double[] targetValues = {0.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithSolutionAtBoundary(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {-1.0, 0.0});
        Assert.assertArrayEquals(new double[] {0.0, 1.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a function where the optimal solution is at the
     * corner of the domain.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithSolutionAtCorner() throws Exception {
        double[] targetValues = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithSolutionAtCorner(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {0.0, 0.0});
        Assert.assertArrayEquals(new double[] {1.0, 1.0}, result.getValue(), 1e-12);
    }

    /**
     * Test the optimizer with a function where the optimal solution is on the
     * edge of the domain.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testFunctionWithSolutionOnEdge() throws Exception {
        double[] targetValues = {1.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new FunctionWithSolutionOnEdge(targetValues);
        PointValuePair result = optimizer.optimize(function, weightMatrix, new double[] {1.0, 1.0e-10});
        Assert.assertArrayEquals(new double[] {1.0, 0.0}, result.getValue(), 1e-12);
    }
}