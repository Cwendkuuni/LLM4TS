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
import static org.mockito.Mockito.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimizeThrowsNullArgumentExceptionWhenConvergenceCheckerIsNull() {
        assertThrows(NullArgumentException.class, () -> {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.setConvergenceChecker(null);
            optimizer.doOptimize();
        });
    }

    @Test
    public void testDoOptimizeThrowsMathInternalErrorWhenConvergenceIsNotReached() {
        assertThrows(MathInternalError.class, () -> {
            ConvergenceChecker<PointVectorValuePair> checker = mock(ConvergenceChecker.class);

            double[][] jacobian = {{1, 2}, {3, 4}};
            double[] startPoint = {1, 1};
            double[] target = {5, 7};

            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
            optimizer.setJacobian(jacobian);
            optimizer.setStartPoint(startPoint);
            optimizer.setTarget(target);

            when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(false);

            optimizer.doOptimize();
        });
    }

    @Test
    public void testDoOptimizeThrowsConvergenceExceptionWhenSingularMatrixExceptionIsThrown() {
        assertThrows(ConvergenceException.class, () -> {
            double[][] jacobian = {{1, 2}, {3, 4}};
            double[] startPoint = {1, 1};
            double[] target = {5, 7};

            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.setJacobian(jacobian);
            optimizer.setStartPoint(startPoint);
            optimizer.setTarget(target);

            optimizer.doOptimize();
        });
    }

    @Test
    public void testDoOptimizeReturnsCorrectResultWhenConvergenceIsReached() {
        double[][] jacobian = {{1, 2}, {3, 4}};
        double[] startPoint = {1, 1};
        double[] target = {5, 7};

        PointVectorValuePair expected = new PointVectorValuePair(new double[]{2, 1}, new double[]{0, 0});

        ConvergenceChecker<PointVectorValuePair> checker = mock(ConvergenceChecker.class);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setJacobian(jacobian);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setConvergenceChecker(checker);

        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(true);

        PointVectorValuePair result = optimizer.doOptimize();

        assertArrayEquals(expected.getPoint(), result.getPoint(), 0);
        assertArrayEquals(expected.getValue(), result.getValue(), 0);
    }
}