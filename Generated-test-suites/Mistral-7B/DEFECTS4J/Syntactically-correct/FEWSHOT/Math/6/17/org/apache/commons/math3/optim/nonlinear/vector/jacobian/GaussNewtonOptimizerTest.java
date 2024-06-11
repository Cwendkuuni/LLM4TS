package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.PointVector;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.VectorFormat;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithLUDecomposition() throws ConvergenceException, SingularMatrixException {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {2.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 2.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        PointVectorValuePair solution = optimizer.optimize();
        PointValuePair pointValuePair = solution.getPointValuePair();
        double[] expectedSolution = new double[]{1.5 - 0.136, 1.5 - 0.136, 1.5 - 0.136};
        assertEquals(expectedSolution[0], pointValuePair.getPoint()[0], 0.001);
        assertEquals(expectedSolution[1], pointValuePair.getPoint()[1], 0.001);
        assertEquals(expectedSolution[2], pointValuePair.getPoint()[2], 0.001);
        assertEquals(0.0, pointValuePair.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithQRDecomposition() throws ConvergenceException, SingularMatrixException {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {2.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 2.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        PointVectorValuePair solution = optimizer.optimize();
        PointValuePair pointValuePair = solution.getPointValuePair();
        double[] expectedSolution = new double[]{1.5 - 0.119, 1.5 - 0.119, 1.5 - 0.119};
        assertEquals(expectedSolution[0], pointValuePair.getPoint()[0], 0.001);
        assertEquals(expectedSolution[1], pointValuePair.getPoint()[1], 0.001);
        assertEquals(expectedSolution[2], pointValuePair.getPoint()[2], 0.001);
        assertEquals(0.0, pointValuePair.getValue(), 0.001);
    }

    @Test
    public void testSingularMatrixException() {
        double[] targetValues = new double[]{1.0};
        double[] startPoint = new double[]{1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {0.0, 0.0},
            {0.0, 0.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        try {
            optimizer.optimize();
            fail("Expected ConvergenceException but got no exception.");
        } catch (ConvergenceException e) {
            if (!(e.getMessage().contains("UNABLE_TO_SOLVE_SINGULAR_PROBLEM"))) {
                fail("Unexpected exception message: " + e.getMessage());
            }
        }
    }

    @Test
    public void testComputeCost() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] currentPoint = new double[]{1.5, 1.5, 1.5};
        double[] currentResiduals = new double[]{0.5, 0.5, 0.5};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(currentPoint);

        double expectedCost = 1.5 * (Math.pow(0.5, 2) + Math.pow(0.5, 2) + Math.pow(0.5, 2));
        assertEquals(expectedCost, optimizer.computeCost(currentResiduals), 0.001);
    }
}