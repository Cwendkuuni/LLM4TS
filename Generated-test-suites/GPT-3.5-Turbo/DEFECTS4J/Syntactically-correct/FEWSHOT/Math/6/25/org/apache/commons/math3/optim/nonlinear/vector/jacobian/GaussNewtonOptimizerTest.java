import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
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
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GaussNewtonOptimizerTest {

    @Mock
    private ConvergenceChecker<PointVectorValuePair> checkerMock;

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        optimizer = new GaussNewtonOptimizer(checkerMock);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeMathInternalError() {
        when(checkerMock.converged(ArgumentMatchers.anyInt(), any(), any())).thenReturn(false);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeConverged() {
        final int nR = 2;
        final double[] targetValues = new double[] {1.0, 2.0};
        optimizer.withStartPoint(new double[] {1.0})
                 .withTarget(targetValues)
                 .withWeight(MatrixUtils.createRealIdentityMatrix(nR))
                 .withChecker(new SimpleVectorValueChecker());
        
        when(checkerMock.converged(ArgumentMatchers.anyInt(), any(), any())).thenReturn(true);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertTrue(result != null);
        assertEquals(1.0, result.getPoint()[0], 0.0);
        assertEquals(1.0, result.getValue()[0], 0.0);
        assertEquals(nR, optimizer.getCost(), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrixException() {
        final int nR = 2;
        final double[] targetValues = new double[] {1.0, 2.0};
        optimizer.withStartPoint(new double[] {1.0})
                 .withTarget(targetValues)
                 .withWeight(MatrixUtils.createRealIdentityMatrix(nR))
                 .withChecker(new SimpleVectorValueChecker());

        when(checkerMock.converged(ArgumentMatchers.anyInt(), any(), any())).thenReturn(false);

        DecompositionSolver solverMock = mock(DecompositionSolver.class);
        when(solverMock.solve(any(ArrayRealVector.class))).thenThrow(SingularMatrixException.class);

        RealMatrix mA = new BlockRealMatrix(1, 1);
        DecompositionSolver mockSolver = mock(LUDecomposition.class);
        when(mockSolver.getSolver()).thenReturn(solverMock);

        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        final int nR = 2;
        final double[] targetValues = new double[] {1.0, 2.0};
        optimizer.withStartPoint(new double[] {1.0})
                 .withTarget(targetValues)
                 .withWeight(MatrixUtils.createRealIdentityMatrix(nR))
                 .withChecker(new SimpleVectorValueChecker());

        when(checkerMock.converged(ArgumentMatchers.anyInt(), any(), any())).thenReturn(false);

        DecompositionSolver solverMock = mock(DecompositionSolver.class);
        when(solverMock.solve(any(ArrayRealVector.class))).thenReturn(new ArrayRealVector(new double[] {1.0}));

        RealMatrix mA = new BlockRealMatrix(1, 1);
        LUDecomposition mockDecomposition = mock(LUDecomposition.class);
        when(mockDecomposition.getSolver()).thenReturn(solverMock);

        optimizer.doOptimize();
    }
}