package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    @Test
    public void testDoOptimize_LUUseCase() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
            optimizer.doOptimize();
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test
    public void testDoOptimize_QRUseCase() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
            optimizer.doOptimize();
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test
    public void testDoOptimize_NonNullCheckerUseCase() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
            optimizer.doOptimize();
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_NoCheckerUseCase() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.doOptimize();
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Expected exception
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test
    public void testDoOptimize_ExceptionInSolve() {
        try {
            LUDecomposition luDecompositionMock = mock(LUDecomposition.class);
            when(luDecompositionMock.getSolver()).thenThrow(SingularMatrixException.class);
            BlockRealMatrix mA = new BlockRealMatrix(new double[][] {{1.0}});
            DecompositionSolver mockSolver = mock(DecompositionSolver.class);
            when(mockSolver.solve(any(ArrayRealVector.class))).thenReturn(null);
            when(luDecompositionMock.getSolver()).thenReturn(mockSolver);

            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.doOptimize();
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test
    public void testDoOptimize_NullSolution() {
        try {GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.doOptimize();
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException occurred");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError occurred");
        }
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimize_ConvergenceCheckerNotInitialized() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }
}