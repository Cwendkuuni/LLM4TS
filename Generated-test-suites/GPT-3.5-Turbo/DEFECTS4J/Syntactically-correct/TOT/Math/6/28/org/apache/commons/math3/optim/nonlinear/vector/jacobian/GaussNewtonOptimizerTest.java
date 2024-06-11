import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GaussNewtonOptimizerTest {

    @Mock
    private ConvergenceChecker<PointVectorValuePair> checker;

    private double[] targetValues;
    private RealMatrix weightMatrix;
    private double[] currentPoint;
    private double[] startObjective;
    private double[] startResiduals;
    private RealMatrix weightedJacobian;
    private double[] currentObjective;
    private double[] currentResiduals;
    private double[] bValues;
    private double[][] aValues;
    private RealMatrix mA;
    private RealVector bVector;
    private RealVector dXVector;

    @Before
    public void setup() {
        targetValues = new double[]{1, 2, 3};
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        currentPoint = new double[]{1, 1, 1};
        startObjective = new double[]{2, 3, 4};
        startResiduals = new double[]{1, 1, 1};
        weightedJacobian = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        currentObjective = new double[]{2, 2, 2};
        currentResiduals = new double[]{0, 0, 0};
        bValues = new double[]{0, 0, 0};
        aValues = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        mA = new Array2DRowRealMatrix(aValues);
        bVector = MatrixUtils.createRealVector(bValues);
        dXVector = MatrixUtils.createRealVector(new double[]{0, 0, 0});
    }

    @Test
    public void testDoOptimize_withConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(currentPoint);

        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(true);

        PointVectorValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        verify(checker, times(1)).converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class));
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_withoutConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(currentPoint);

        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_withLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(currentPoint);

        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(true);

        PointVectorValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        verify(checker, times(1)).converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class));
    }

    @Test
    public void testDoOptimize_withQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(currentPoint);

        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(true);

        PointVectorValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        verify(checker, times(1)).converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class));
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_withSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(currentPoint);

        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(false);
        when(checker.converged(anyInt(), any(PointVectorValuePair.class), any(PointVectorValuePair.class))).thenReturn(true);

        optimizer.doOptimize();
    }
}