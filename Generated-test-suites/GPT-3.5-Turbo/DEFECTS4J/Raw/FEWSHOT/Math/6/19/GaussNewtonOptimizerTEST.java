
package org.apache.commons.math3.optimization.general;

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

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {
    // Test Constants
    private static final double EPSILON = 1e-6;

    private static final double[] START_POINT = {1.0, 2.0};
    private static final double[] TARGET = {3.0, 4.0};
    private static final RealMatrix WEIGHT = new BlockRealMatrix(new double[][]{{1, 0}, {0, 1}});

    @Test
    public void testDoOptimize() {
        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());

        // Set parameters
        optimizer.setStartPoint(START_POINT);
        optimizer.setTarget(TARGET);
        optimizer.setWeight(WEIGHT);

        // Optimize
        PointVectorValuePair result = optimizer.doOptimize();

        // Check result
        double[] point = result.getPoint();
        double[] objective = result.getValue();
        assertEquals(TARGET[0], objective[0], EPSILON);
        assertEquals(TARGET[1], objective[1], EPSILON);
        assertEquals(START_POINT[0], point[0], EPSILON);
        assertEquals(START_POINT[1], point[1], EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullConvergenceChecker() {
        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set parameters
        optimizer.setStartPoint(START_POINT);
        optimizer.setTarget(TARGET);
        optimizer.setWeight(WEIGHT);

        // Optimize
        PointVectorValuePair result = optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrixException() {
        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set parameters
        double[] startPoint = {1.0, 1.0, 1.0};
        double[] target = {2.0, 3.0, 4.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(WEIGHT);

        // Optimize
        PointVectorValuePair result = optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeWithNoConvergence() {
        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return false;
            }
        });

        // Set parameters
        optimizer.setStartPoint(START_POINT);
        optimizer.setTarget(TARGET);
        optimizer.setWeight(WEIGHT);

        // Optimize
        PointVectorValuePair result = optimizer.doOptimize();
    }
}
