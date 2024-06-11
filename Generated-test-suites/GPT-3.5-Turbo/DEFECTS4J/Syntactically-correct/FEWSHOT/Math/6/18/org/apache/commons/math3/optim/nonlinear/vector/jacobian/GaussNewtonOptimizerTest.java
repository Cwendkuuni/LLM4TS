package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    private static final double EPSILON = 1e-10;
    
    @Test
    public void testDoOptimize() {
        // Create a test instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        
        // Create dummy data
        double[] target = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weight = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}});
        
        // Set the data to the optimizer
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weight);
        
        // Call doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Check the result
        double[] expectedPoint = {1.0, 1.0};
        assertArrayEquals(expectedPoint, result.getPoint(), EPSILON);
        
        // Check that the cost is computed correctly
        double[] objective = optimizer.computeObjectiveValue(result.getPoint());
        double[] residuals = optimizer.computeResiduals(objective);
        double expectedCost = computeCost(residuals);
        assertEquals(expectedCost, optimizer.getCost(), EPSILON);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.doOptimize();
    }
    
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] target = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weight = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 0.0}});
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weight);
        optimizer.doOptimize();
    }
    
    private double computeCost(double[] residuals) {
        double cost = 0;
        for (double residual : residuals) {
            cost += Math.pow(residual, 2);
        }
        return cost;
    }
}