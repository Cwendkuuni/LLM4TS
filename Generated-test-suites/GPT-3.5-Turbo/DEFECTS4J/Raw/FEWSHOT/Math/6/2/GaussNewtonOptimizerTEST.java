
package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

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
    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        optimizer.setTarget(new double[]{1.0, 2.0});
        
        try {
            optimizer.doOptimize();
        } catch (ConvergenceException e) {
            // It is expected that the optimizer will throw a ConvergenceException.
            assertTrue(e.getMessage().contains(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM.toString()));
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException");
        } catch (MathInternalError e) {
            fail("Unexpected MathInternalError");
        }
    }
}
