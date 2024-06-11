import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
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

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrixException() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);

        double[] startPoint = {1.0, 1.0, 1.0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);

        double[] startPoint = {1.0, 1.0, 1.0};

        ConvergenceChecker<PointVectorValuePair> checker = mock(ConvergenceChecker.class);
        when(checker.converged(1, null, null)).thenReturn(true);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();

        assertNull(result);
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeThrowsMathInternalError() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);

        double[] startPoint = {1.0, 1.0, 1.0};

        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNull(optimizer.getConvergenceChecker());
    }
}